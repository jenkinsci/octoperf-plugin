package org.jenkinsci.plugins.octoperf;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import jenkins.model.Jenkins;
import lombok.Getter;
import lombok.Setter;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static hudson.model.Result.SUCCESS;
import static hudson.tasks.BuildStepMonitor.BUILD;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;
import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;
import static org.jenkinsci.plugins.octoperf.junit.JUnitReportService.JUNIT_REPORTS;
import static org.jenkinsci.plugins.octoperf.log.LogService.LOGS;
import static org.jenkinsci.plugins.octoperf.metrics.MetricsService.METRICS;
import static org.jenkinsci.plugins.octoperf.report.BenchReportService.BENCH_REPORTS;
import static org.jenkinsci.plugins.octoperf.result.BenchResultService.BENCH_RESULTS;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.ABORTED;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.ERROR;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;
import static org.joda.time.format.DateTimeFormat.forPattern;

@Getter
@Setter
public class OctoperfBuilder extends Builder {
  @Extension
  public static final OctoperfBuilderDescriptor DESCRIPTOR = new OctoperfBuilderDescriptor();

  private static final DateTimeFormatter DATE_FORMAT = forPattern("HH:mm:ss");
  public static final long TEN_SECS = 10_000L;

  private final Optional<OctoperfCredential> credentials;
  private final String scenarioId;
  private List<? extends TestStopCondition> stopConditions = new ArrayList<>();

  @DataBoundConstructor
  public OctoperfBuilder(
      final String credentialsId, 
      final String scenarioId) {
    super();
    this.credentials = CREDENTIALS_SERVICE.find(credentialsId);
    this.scenarioId = checkNotNull(scenarioId);
  }

  @Override
  public BuildStepMonitor getRequiredMonitorService() {
    return BUILD;
  }
  
  @Override
  public boolean perform(
      final AbstractBuild<?, ?> build, 
      final Launcher launcher,
      final BuildListener listener) throws InterruptedException, IOException {
    final PrintStream logger = listener.getLogger();

    final OctoperfCredential creds = credentials.orElse(null);
    logger.println("Username=" + creds.getUsername());

    final String apiUrl = OctoperfBuilder.DESCRIPTOR.getOctoperfURL();
    logger.println("API url: " + apiUrl);
    
    logger.println("Logging in..");
    final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(apiUrl, logger);
    pair.getRight().onUsernameAndPassword(creds.getUsername(), creds.getPassword().getPlainText());
    final RestApiFactory apiFactory = pair.getLeft();

    final Scenario scenario = SCENARIOS.find(apiFactory, scenarioId);
    logger.println("Launching test with:");
    logger.println("- name: " + scenario.getName() + ",");
    logger.println("- description: " + scenario.getDescription());

    final BenchResult result;

    BenchReport report;
    try {
      report = SCENARIOS.startTest(apiFactory, scenarioId);
      result = BENCH_RESULTS.find(apiFactory, report.getBenchResultId());
      logger.println("Starting test...");

      final Project project = ProjectService.PROJECTS.find(apiFactory, scenario.getProjectId());
      logger.println("Bench report is available at: " + BENCH_REPORTS.getReportUrl(
        apiUrl,
        project.getWorkspaceId(),
        result.getResultProjectId(),
        report));
    } catch(final IOException e) {
      logger.println("Could not start test: " + e);
      e.printStackTrace(logger);
      return false;
    }
    
    logger.println("Launching test..");
    BenchResultState currentState;

    java.util.Optional<DateTime> startTime = empty();
    while(true) {
      Thread.sleep(TEN_SECS);
      
      currentState = BENCH_RESULTS.getState(apiFactory, result.getId());

      if(currentState.isRunning()) {
        for (final TestStopCondition condition : stopConditions) {
          condition.execute(logger, build, apiFactory, result);
        }

        final DateTime now = DateTime.now();
        if(!startTime.isPresent()) {
          startTime = of(now);
        }
        
        final MetricValues metrics = METRICS.getMetrics(apiFactory, result.getId());
        final String printable = METRICS.toPrintable(startTime.get(), metrics);
        final String nowStr = DATE_FORMAT.print(now);

        final String progress = String.format("[%.2f%%] ", BENCH_RESULTS.getProgress(apiFactory, result.getId()));
        logger.println(progress + nowStr + " - " + printable);
      } else if(currentState.isTerminalState()) {
        logger.println("Test finished with state: " + currentState);
        break;
      } else {
        logger.println("Preparing test.. (" + currentState +")");
      }
    }
    
    final FilePath workspace = build.getWorkspace();

    logger.println("Saving JUnit report...");
    final FilePath junitReport = JUNIT_REPORTS.saveJUnitReport(workspace, apiFactory, result.getId());
    logger.println("JUnit report saved to: " + junitReport);
    
    logger.println("Downloading JMeter Logs and JTLs...");
    LOGS.downloadLogFiles(workspace, logger, apiFactory, result.getId());

    logger.println("Merging JTLs into a single file...");
    LOGS.mergeJTLs(workspace, logger);
    
    if(currentState == ERROR) {
      build.setResult(Result.FAILURE);
    } else if(currentState == ABORTED) {
      build.setResult(Result.ABORTED);
    } else {
      build.setResult(SUCCESS);
    }
    
    return true;
  }

  @Override
  public OctoperfBuilderDescriptor getDescriptor() {
    return DESCRIPTOR;
  }

  public List<? extends TestStopCondition> getStopConditions() {
    return stopConditions;
  }

  @DataBoundSetter
  public void setStopConditions(List<? extends TestStopCondition> stopConditions) {
    this.stopConditions = ofNullable(stopConditions).orElse(new ArrayList<>());
  }

  /**
   * Returns all the registered {@link Builder} descriptors.
   */
  // for backward compatibility, the signature is not BuildStepDescriptor
  public static DescriptorExtensionList<Builder,Descriptor<Builder>> all() {
    return Jenkins.getInstance().<Builder,Descriptor<Builder>>getDescriptorList(Builder.class);
  }

}
