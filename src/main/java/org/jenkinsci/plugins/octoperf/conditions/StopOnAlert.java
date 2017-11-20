package org.jenkinsci.plugins.octoperf.conditions;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.util.ListBoxModel;
import lombok.Getter;
import lombok.Setter;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.result.BenchResult;
import org.jenkinsci.plugins.octoperf.threshold.ThresholdSeverity;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.PrintStream;

import static hudson.model.Result.ABORTED;
import static hudson.model.Result.FAILURE;
import static hudson.model.Result.UNSTABLE;
import static java.util.Optional.ofNullable;
import static org.jenkinsci.plugins.octoperf.result.BenchResultService.BENCH_RESULTS;
import static org.jenkinsci.plugins.octoperf.threshold.ThresholdAlarmService.THRESHOLD_ALARMS;
import static org.jenkinsci.plugins.octoperf.threshold.ThresholdSeverity.CRITICAL;
import static org.jenkinsci.plugins.octoperf.threshold.ThresholdSeverity.WARNING;

@Getter
@Setter
public class StopOnAlert extends TestStopCondition {
  private ThresholdSeverity severity = WARNING;
  private Result buildResult = ABORTED;

  @DataBoundConstructor
  public StopOnAlert(final ThresholdSeverity severity, final Result buildResult) {
    super();
    this.severity = ofNullable(severity).orElse(WARNING);
    this.buildResult = ofNullable(buildResult).orElse(UNSTABLE);
  }

  @Override
  public void execute(
    final PrintStream logger,
    final AbstractBuild<?, ?> build,
    final RestApiFactory factory,
    final BenchResult result) throws IOException {
    if(THRESHOLD_ALARMS.hasAlarms(factory, result.getId(), severity)) {
      logger.println("An '" + severity + "' Alarm has been raised, Stopping the test!");
      build.setResult(buildResult);
      BENCH_RESULTS.stopTest(factory, result);
    }
  }

  @Symbol("stoponalert")
  @Extension
  public static class DescriptorImpl extends StopConditionDescriptor {

    @Override
    public String getDisplayName() {
      return "Stop On Alert";
    }

    public ListBoxModel doFillSeverityItems() {
      final ListBoxModel items = new ListBoxModel();

      items.add("Warning", WARNING.toString());
      items.add("Critical", CRITICAL.toString());

      return items;
    }

    public ListBoxModel doFillBuildResultItems() {
      final ListBoxModel items = new ListBoxModel();

      items.add("Unstable", UNSTABLE.toString());
      items.add("Failure", FAILURE.toString());
      items.add("Aborted", ABORTED.toString());

      return items;
    }
  }
}