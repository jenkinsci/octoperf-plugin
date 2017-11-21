package org.jenkinsci.plugins.octoperf;

import com.google.common.collect.ImmutableList;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.remoting.Callable;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import lombok.Getter;
import lombok.Setter;
import org.jenkinsci.plugins.octoperf.conditions.TestStopCondition;
import org.joda.time.format.DateTimeFormatter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hudson.tasks.BuildStepMonitor.BUILD;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;
import static org.joda.time.format.DateTimeFormat.forPattern;

@Getter
@Setter
public class OctoperfBuilder extends Builder implements SimpleBuildStep {
  private static final DateTimeFormatter DATE_FORMAT = forPattern("HH:mm:ss");
  public static final long TEN_SECS = 10_000L;

  private final Optional<OctoperfCredential> credentials;
  private final String scenarioId;
  private String serverUrl = "";
  private List<? extends TestStopCondition> stopConditions = new ArrayList<>();

  @DataBoundConstructor
  public OctoperfBuilder(
    final String credentialsId,
    final String scenarioId) {
    super();
    this.credentials = CREDENTIALS_SERVICE.find(credentialsId);
    this.scenarioId = requireNonNull(scenarioId);
    this.serverUrl = requireNonNull(serverUrl);
  }

  @Override
  public BuildStepMonitor getRequiredMonitorService() {
    return BUILD;
  }

  public List<? extends TestStopCondition> getStopConditions() {
    return stopConditions;
  }

  @DataBoundSetter
  public void setStopConditions(List<? extends TestStopCondition> stopConditions) {
    this.stopConditions = ofNullable(stopConditions).orElse(new ArrayList<>());
  }

  @Override
  public void perform(
    @Nonnull final Run<?, ?> run,
    @Nonnull final FilePath workspace,
    @Nonnull final Launcher launcher,
    @Nonnull final TaskListener listener) throws InterruptedException, IOException {
    perform(run, workspace, listener, new EnvVars());
  }

  public void perform(
    @Nonnull final Run<?, ?> run,
    @Nonnull final FilePath workspace,
    @Nonnull final TaskListener listener,
    @Nonnull final EnvVars vars) throws InterruptedException, IOException {
    final String serverUrlConfig = OctoperfBuilderDescriptor.getDescriptor().getOctoperfURL();

    final Callable<Result, Exception> build = new OctoPerfBuild(
      listener.getLogger(),
      credentials.map(OctoperfCredential::getUsername).orElse(""),
      credentials.map(OctoperfCredential::getPassword).orElse(null),
      scenarioId,
      ImmutableList.copyOf(stopConditions),
      workspace,
      serverUrlConfig != null ? serverUrlConfig : serverUrl,
      vars
    );

    Result result;
    try {
      result = build.call();
    } catch (final Exception e) {
      result = Result.FAILURE;
      listener.fatalError("Error while running test: " + e);
    }

    run.setResult(result);
  }
}
