package org.jenkinsci.plugins.octoperf;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ListBoxModel;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.conditions.StopConditionDescriptor;
import org.jenkinsci.plugins.octoperf.conditions.TestStopCondition;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
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

@Data
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
  public StepExecution start(final StepContext stepContext) throws Exception {
    return new OctoPerfTestExecution(stepContext, credentialsId, scenarioId, serverUrl, stopConditions);
  }

  public static class OctoPerfTestExecution extends SynchronousNonBlockingStepExecution<Void> {

    private final String credentialsId;
    private final String scenarioId;
    private final String serverUrl;
    private List<? extends TestStopCondition> stopConditions = new ArrayList<>();

    private transient StepContext context;

    private EnvVars variables = null;

    protected OctoPerfTestExecution(
      @Nonnull final StepContext context,
      @Nonnull final String credentialsId,
      @Nonnull final String scenarioId,
      @Nonnull final String serverUrl,
      @Nonnull final List<? extends TestStopCondition> stopConditions) {
      super(context);
      this.context = context;
      this.credentialsId = requireNonNull(credentialsId);
      this.scenarioId = requireNonNull(scenarioId);
      this.serverUrl = requireNonNull(serverUrl);
      this.stopConditions = requireNonNull(stopConditions);
    }

    @Override
    protected Void run() throws Exception {
      final OctoperfBuilder builder = new OctoperfBuilder(
        this.credentialsId,
        this.scenarioId
      );
      builder.setServerUrl(serverUrl);
      builder.setStopConditions(stopConditions);

      final Run run = this.context.get(Run.class);
      final FilePath workspace = this.context.get(FilePath.class);
      final TaskListener listener = this.context.get(TaskListener.class);
      this.variables = this.context.get(EnvVars.class);
      builder.perform(run, workspace, listener, variables);
      return null;
    }

    @Override
    public void stop(final Throwable cause) throws Exception {
      context.onFailure(cause);

      final OctoperfCredential credentials = CREDENTIALS_SERVICE.find(credentialsId).orElse(null);
      final TaskListener listener = this.context.get(TaskListener.class);

      final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(serverUrl, listener.getLogger());
      pair.getRight().onUsernameAndPassword(credentials.getUsername(), credentials.getPassword().getPlainText());
      final RestApiFactory apiFactory = pair.getLeft();

      final String benchResultId = variables.getOrDefault("BENCH_RESULT_ID", "");
      if (!benchResultId.isEmpty()) {
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

    public ListBoxModel doFillCredentialsIdItems(final Object scope) {
      return OctoperfBuilderDescriptor.getDescriptor().doFillCredentialsIdItems(scope);
    }

    /**
     * Computes the scenarios list when asked by the UI.
     *
     * @param credentialsId credentials id
     * @return combo box model with all scenarios
     */
    public ListBoxModel doFillScenarioIdItems(@QueryParameter("credentialsId") final String credentialsId) {
      return OctoperfBuilderDescriptor.getDescriptor().doFillScenarioIdItems(credentialsId);
    }

    public List<StopConditionDescriptor> getStopConditionDescriptors() {
      return StopConditionDescriptor.all();
    }
  }
}
