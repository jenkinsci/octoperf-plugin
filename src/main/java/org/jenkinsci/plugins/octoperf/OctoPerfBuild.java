package org.jenkinsci.plugins.octoperf;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.Result;
import hudson.remoting.Callable;
import hudson.util.Secret;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.conditions.TestStopCondition;
import org.jenkinsci.plugins.octoperf.metrics.MetricValues;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.project.ProjectService;
import org.jenkinsci.plugins.octoperf.report.BenchReport;
import org.jenkinsci.plugins.octoperf.result.BenchResult;
import org.jenkinsci.plugins.octoperf.result.BenchResultState;
import org.jenkinsci.plugins.octoperf.scenario.Scenario;
import org.jenkinsci.remoting.RoleChecker;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static hudson.model.Result.FAILURE;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lombok.AccessLevel.PRIVATE;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;
import static org.jenkinsci.plugins.octoperf.junit.JUnitReportService.JUNIT_REPORTS;
import static org.jenkinsci.plugins.octoperf.log.LogService.LOGS;
import static org.jenkinsci.plugins.octoperf.metrics.MetricsService.METRICS;
import static org.jenkinsci.plugins.octoperf.report.BenchReportService.BENCH_REPORTS;
import static org.jenkinsci.plugins.octoperf.result.BenchResultService.BENCH_RESULTS;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.ABORTED;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.ERROR;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;
import static org.joda.time.format.DateTimeFormat.forPattern;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OctoPerfBuild implements Callable<Result, Exception> {
  private static final DateTimeFormatter DATE_FORMAT = forPattern("HH:mm:ss");
  public static final long TEN_SECS = 10_000L;

  PrintStream logger;
  String username;
  Secret password;
  String scenarioId;
  List<? extends TestStopCondition> stopConditions;
  FilePath workspace;
  String serverUrl;
  EnvVars envVars;

  OctoPerfBuild(
    final PrintStream logger,
    final String username,
    final Secret password,
    final String scenarioId,
    final List<? extends TestStopCondition> stopConditions,
    final FilePath workspace,
    final String serverUrl,
    final EnvVars envVars) {
    super();
    this.logger = requireNonNull(logger);
    this.username = requireNonNull(username);
    this.password = requireNonNull(password);
    this.scenarioId = requireNonNull(scenarioId);
    this.stopConditions = requireNonNull(stopConditions);
    this.workspace = requireNonNull(workspace);
    this.serverUrl = requireNonNull(serverUrl);
    this.envVars = requireNonNull(envVars);
  }

  @Override
  public Result call() throws Exception {
    Result result = Result.SUCCESS;

    logger.println("Username: " + username);

    logger.println("API Endpoint: " + serverUrl);

    final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(serverUrl, logger);
    pair.getRight().onUsernameAndPassword(username, password.getPlainText());
    final RestApiFactory apiFactory = pair.getLeft();

    final Scenario scenario = SCENARIOS.find(apiFactory, scenarioId);
    logger.println("Launching Scenario with:");
    logger.println("- name: " + scenario.getName() + ",");
    if (!scenario.getDescription().isEmpty()) {
      logger.println("- description: " + scenario.getDescription());
    }

    final BenchResult benchResult;

    BenchReport report;
    try {
      report = SCENARIOS.startTest(apiFactory, scenarioId);
      benchResult = BENCH_RESULTS.find(apiFactory, report.getBenchResultId());

      logger.println("Starting test...");

      final Project project = ProjectService.PROJECTS.find(apiFactory, scenario.getProjectId());
      logger.println("Bench Report: " + BENCH_REPORTS.getReportUrl(
        serverUrl,
        project.getWorkspaceId(),
        benchResult.getResultProjectId(),
        report));
    } catch(final IOException e) {
      logger.println("Could not start test: " + e);
      return FAILURE;
    }

    envVars.put("BENCH_RESULT_ID", benchResult.getId());

    logger.println("Launching test..");
    BenchResultState currentState;

    java.util.Optional<DateTime> startTime = empty();
    while(true) {
      Thread.sleep(TEN_SECS);

      currentState = BENCH_RESULTS.getState(apiFactory, benchResult.getId());

      if(currentState.isRunning()) {
        for (final TestStopCondition condition : stopConditions) {
          result = condition.execute(logger, apiFactory, benchResult.getId());
        }

        final DateTime now = DateTime.now();
        if(!startTime.isPresent()) {
          startTime = of(now);
        }

        final MetricValues metrics = METRICS.getMetrics(apiFactory, benchResult.getId());
        final String printable = METRICS.toPrintable(startTime.get(), metrics);
        final String nowStr = DATE_FORMAT.print(now);

        final String progress = String.format("[%.2f%%] ", BENCH_RESULTS.getProgress(apiFactory, benchResult.getId()));
        logger.println(progress + nowStr + " - " + printable);
      } else if(currentState.isTerminalState()) {
        logger.println("Test finished with state: " + currentState);
        break;
      } else {
        logger.println("Preparing test.. (" + currentState +")");
      }
    }

    envVars.remove("BENCH_RESULT_ID");

    logger.println("Saving JUnit report...");
    final FilePath junitReport = JUNIT_REPORTS.saveJUnitReport(workspace, apiFactory, benchResult.getId());
    logger.println("JUnit report saved to: " + junitReport);

    logger.println("Downloading JMeter Logs and JTLs...");
    LOGS.downloadLogFiles(workspace, logger, apiFactory, benchResult.getId());

    logger.println("Merging JTLs into a single file...");
    LOGS.mergeJTLs(workspace, logger);

    if(currentState == ERROR) {
      return Result.FAILURE;
    } else if(currentState == ABORTED) {
      return Result.ABORTED;
    }

    return result;
  }

  @Override
  public void checkRoles(final RoleChecker roleChecker) throws SecurityException {
    // Nothing to do
  }
}
