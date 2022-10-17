package org.jenkinsci.plugins.octoperf;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ListBoxModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.conditions.StopConditionDescriptor;
import org.jenkinsci.plugins.octoperf.conditions.TestStopCondition;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.ImmutableList.of;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;
import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;
import static org.jenkinsci.plugins.octoperf.result.BenchResultService.BENCH_RESULTS;

@Getter
@Setter
public class OctoPerfTestStep extends Step {
  private String credentialsId = "";
  private String scenarioId = "";
  private List<? extends TestStopCondition> stopConditions = new ArrayList<>();
  private String serverUrl = "";

  @DataBoundConstructor
  public OctoPerfTestStep(
    final String credentialsId,
    final String scenarioId) {
    super();
    setCredentialsId(credentialsId);
    setScenarioId(scenarioId);
    setServerUrl("");
  }

  @DataBoundSetter
  public void setCredentialsId(final String credentialsId) {
    this.credentialsId = nullToEmpty(credentialsId);
  }

  @DataBoundSetter
  public void setScenarioId(final String scenarioId) {
    this.scenarioId = nullToEmpty(scenarioId);
  }

  @DataBoundSetter
  public void setServerUrl(final String serverUrl) {
    this.serverUrl = OctoperfBuilderDescriptor.getDescriptor().getOctoperfURL();
  }

  @DataBoundSetter
  public void setStopConditions(final List<? extends TestStopCondition> stopConditions) {
    this.stopConditions = ofNullable(stopConditions).orElse(of());
  }

  @Override
  public StepExecution start(final StepContext stepContext) {
    return new OctoPerfTestExecution(
      stepContext,
      credentialsId,
      scenarioId,
      serverUrl,
      stopConditions
    );
  }

  public static class OctoPerfTestExecution extends SynchronousNonBlockingStepExecution<Void> {
    private static final long serialVersionUID = -3802154812289490186L;

    private final String credentialsId;
    private final String scenarioId;
    private final String serverUrl;
    private List<? extends TestStopCondition> stopConditions = new ArrayList<>();

    private EnvVars variables = new EnvVars();

    protected OctoPerfTestExecution(
      @Nonnull final StepContext context,
      @Nonnull final String credentialsId,
      @Nonnull final String scenarioId,
      @Nonnull final String serverUrl,
      @Nonnull final List<? extends TestStopCondition> stopConditions) {
      super(context);
      this.credentialsId = requireNonNull(credentialsId);
      this.scenarioId = requireNonNull(scenarioId);
      this.serverUrl = requireNonNull(serverUrl);
      this.stopConditions = requireNonNull(stopConditions);
    }

    @Override
    protected Void run() throws Exception {
      final OctoperfBuilder builder = new OctoperfBuilder(
        this.credentialsId,
        "",
        "",
        this.scenarioId,
        "",
        this.stopConditions
      );
      builder.setServerUrl(serverUrl);

      final Run run = getContext().get(Run.class);
      final FilePath workspace = getContext().get(FilePath.class);
      final TaskListener listener = getContext().get(TaskListener.class);
      this.variables = getContext().get(EnvVars.class);
      builder.perform(run, workspace, listener, variables);
      return null;
    }

    @Override
    public void stop(final Throwable cause) throws Exception {
      getContext().onFailure(cause);

      final Run run = getContext().get(Run.class);
      final OctoperfCredential credentials = CREDENTIALS_SERVICE.find(credentialsId, run.getParent()).orElse(null);
      final TaskListener listener = getContext().get(TaskListener.class);

      final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(serverUrl, listener.getLogger());
      pair.getRight().onUsernameAndPassword(credentials.getUsername(), credentials.getPassword().getPlainText());
      final RestApiFactory apiFactory = pair.getLeft();

      final String benchResultId = variables.getOrDefault("BENCH_RESULT_ID", "");
      if (benchResultId.isEmpty()) {
        listener.error("Could not stop test: benchResultId is empty");
      } else {
        BENCH_RESULTS.stopTest(apiFactory, benchResultId);
      }
    }
  }

  @Extension
  public static class DescriptorImpl extends StepDescriptor {

    public DescriptorImpl() {
      super();
    }

    @Override
    public Set<? extends Class<?>> getRequiredContext() {
      final Set<Class<?>> r = new HashSet<Class<?>>();
      r.add(Run.class);
      r.add(FilePath.class);
      r.add(Launcher.class);
      r.add(TaskListener.class);
      return r;
    }

    @Override
    public String getFunctionName() {
      return "octoPerfTest";
    }

    @Override
    public String getDisplayName() {
      return "Runs test in OctoPerf Cloud";
    }

    public ListBoxModel doFillCredentialsIdItems(
      @QueryParameter final String credentialsId,
      final Object scope) {
      return OctoperfBuilderDescriptor
        .getDescriptor()
        .doFillCredentialsIdItems(credentialsId, scope);
    }

    public ListBoxModel doFillWorkspaceIdItems(
      @QueryParameter final String credentialsId,
      @QueryParameter final String workspaceId) {
      return OctoperfBuilderDescriptor
        .getDescriptor()
        .doFillWorkspaceIdItems(credentialsId, workspaceId);
    }

    public ListBoxModel doFillProjectIdItems(
      @QueryParameter final String credentialsId,
      @QueryParameter final String workspaceId,
      @QueryParameter final String projectId) {
      return OctoperfBuilderDescriptor
        .getDescriptor()
        .doFillProjectIdItems(credentialsId, workspaceId, projectId);
    }

    public ListBoxModel doFillScenarioIdItems(
      @QueryParameter final String credentialsId,
      @QueryParameter final String projectId,
      @QueryParameter final String scenarioId) {
      return OctoperfBuilderDescriptor
        .getDescriptor()
        .doFillScenarioIdItems(credentialsId, projectId, scenarioId);
    }

    public List<StopConditionDescriptor> getStopConditionDescriptors() {
      return StopConditionDescriptor.all();
    }
  }
}
