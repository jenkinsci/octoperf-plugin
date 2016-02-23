package org.jenkinsci.plugins.octoperf;

import static com.google.common.base.Preconditions.checkNotNull;
import static hudson.model.Result.SUCCESS;
import static hudson.tasks.BuildStepMonitor.BUILD;
import static org.jenkinsci.plugins.octoperf.account.AccountService.ACCOUNTS;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;
import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;
import static org.jenkinsci.plugins.octoperf.junit.JUnitReportService.JUNIT_REPORTS;
import static org.jenkinsci.plugins.octoperf.log.LogService.LOGS;
import static org.jenkinsci.plugins.octoperf.metrics.MetricsService.METRICS;
import static org.jenkinsci.plugins.octoperf.report.BenchReportService.BENCH_REPORTS;
import static org.jenkinsci.plugins.octoperf.result.BenchResultService.BENCH_RESULTS;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.ABORTED;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.ERROR;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.PENDING;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;
import static org.joda.time.format.DateTimeFormat.forPattern;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.metrics.MetricValues;
import org.jenkinsci.plugins.octoperf.result.BenchResult;
import org.jenkinsci.plugins.octoperf.result.BenchResultState;
import org.jenkinsci.plugins.octoperf.scenario.BenchReport;
import org.jenkinsci.plugins.octoperf.scenario.Scenario;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;
import org.kohsuke.stapler.DataBoundConstructor;

import com.google.common.base.Optional;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import lombok.Getter;
import lombok.Setter;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

@Getter
@Setter
public class OctoperfBuilder extends Builder {
  private static final DateTimeFormatter DATE_FORMAT = forPattern("HH:mm:ss");
  
  private final Optional<OctoperfCredential> credentials;
  private final String scenarioId;

  private AbstractBuild<?, ?> build = null;

  @DataBoundConstructor
  public OctoperfBuilder(
      final String credentialsId, 
      final String scenarioId) {
    this.credentials = CREDENTIALS_SERVICE.find(credentialsId);
    this.scenarioId = checkNotNull(scenarioId);
  }

  public BuildStepMonitor getRequiredMonitorService() {
    return BUILD;
  }
  
  @Override
  public boolean perform(
      final AbstractBuild<?, ?> build, 
      final Launcher launcher,
      final BuildListener listener) throws InterruptedException, IOException {
    this.build = build;
    
    final PrintStream logger = listener.getLogger();

    final OctoperfCredential creds = credentials.orNull(); 
    logger.println("Username: " + creds.getUsername());

    final String apiUrl = OctoperfBuilder.DESCRIPTOR.getOctoperfURL();
    logger.println("API url: " + apiUrl);
    
    logger.println("Logging in..");
    final RestAdapter adapter;
    try {
      final Pair<RestAdapter, RestClientAuthenticator> pair = CLIENTS.create(apiUrl);
      adapter = ACCOUNTS.login(pair, creds.getUsername(), creds.getPassword().getPlainText());
      logger.println("Successfully logged in!");
    } catch(final RetrofitError e) {
      logger.println("Login failed: " + String.valueOf(e));
      return false;
    }
    
    final Scenario scenario = SCENARIOS.find(adapter, scenarioId);
    logger.println("Launching scenario with:");
    logger.println("- name: " + scenario.getName() + ",");
    logger.println("- description: " + scenario.getDescription());
    
    BenchReport report;
    try {
      report = SCENARIOS.startTest(adapter, scenarioId);
      logger.println("The scenario has been successfully scheduled for execution!");
      logger.println("Bench report is available at: " + BENCH_REPORTS.getReportUrl(report));
    } catch(final RetrofitError e) {
      logger.println("Could not start test: " + String.valueOf(e));
      return false;
    }
    
    final BenchResult result = BENCH_RESULTS.find(adapter, report.getBenchResultId());
    final Duration duration = Duration.millis(result.getDurationInMs());
    logger.println("Expected test duration: " + duration.toPeriod());
    
    logger.println("Launching test..");
    BenchResultState currentState = PENDING;
    
    Optional<DateTime> startTime = Optional.absent();
    while(true) {
      Thread.sleep(10000);
      
      currentState = BENCH_RESULTS.getState(adapter, report.getBenchResultId());
      
      if(currentState.isRunning()) {
        final DateTime now = DateTime.now();
        if(!startTime.isPresent()) {
          startTime = Optional.of(now);
        }
        
        final MetricValues metrics = METRICS.getMetrics(adapter, result.getId());
        final String printable = METRICS.toPrintable(startTime.get(), metrics);
        logger.println(DATE_FORMAT.print(now) + " - " + printable);
        
      } else if(currentState.isTerminalState()) {
        logger.println("Test finished with state: " + currentState);
        break;
      } else {
        logger.println("Test is starting.. (" + currentState +")");
      }
    }
    
    final FilePath workspace = build.getWorkspace();
    
    logger.println("Saving JUnit report...");
    final FilePath junitReport = JUNIT_REPORTS.saveJUnitReport(workspace, adapter, result.getId());
    logger.println("JUnit report saved to: " + junitReport);
    
    logger.println("Downloading JMeter log files...");
    LOGS.downloadLogFiles(workspace, logger, adapter, result.getId());
    
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

  @Extension
  public static final OctoperfBuilderDescriptor DESCRIPTOR = new OctoperfBuilderDescriptor();
}
