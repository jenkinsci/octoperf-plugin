package org.jenkinsci.plugins.octoperf;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import jenkins.model.Jenkins;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.conditions.StopConditionDescriptor;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.scenario.Scenario;
import org.jenkinsci.plugins.octoperf.workspace.Workspace;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;
import static org.jenkinsci.plugins.octoperf.constants.Constants.DEFAULT_API_URL;
import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;
import static org.jenkinsci.plugins.octoperf.workspace.WorkspaceService.WORKSPACES;

@Extension
@Getter
@Setter
public class OctoperfBuilderDescriptor extends BuildStepDescriptor<Builder> {
  private static final String ARROW = " => ";
  /** Used for default options in dropdowns. */
  protected static final String NONE_ID = "NONE";
  /** Used for default options in dropdowns. */
  protected static final String NONE_DISPLAY_TEXT = "None";

  private String octoperfURL = DEFAULT_API_URL;
  private String name = "My OctoPerf Account";

  public OctoperfBuilderDescriptor() {
    super(OctoperfBuilder.class);
    load();
  }

  static OctoperfBuilderDescriptor getDescriptor() {
    return (OctoperfBuilderDescriptor) Jenkins.get().getDescriptor(OctoperfBuilder.class);
  }

  @Override
  public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
    return true;
  }

  @Override
  public String getDisplayName() {
    return "OctoPerf";
  }

  public ListBoxModel doFillCredentialsIdItems(
    @QueryParameter("credentialsId") final String credentialsId,
    final Object scope) {
    final ListBoxModel items = new ListBoxModel();
    final Set<String> ids = new LinkedHashSet<>();

    final Item item = Stapler.getCurrentRequest().findAncestorObject(Item.class);
    for (final OctoperfCredential c : CREDENTIALS_SERVICE.getCredentials(scope, ofNullable(item))) {
      final String id = c.getId();
      if (!ids.contains(id)) {
        final Option option =
          new Option(c.getUsername(), id, Objects.equals(id, credentialsId));
        ids.add(id);
        items.add(option);
      }
    }

    return getOptions(items);
  }

  public ListBoxModel doFillWorkspaceIdItems(
    @QueryParameter("credentialsId") final String credentialsId,
    @QueryParameter("workspaceId") final String workspaceId) {
    val credentials = getCredential(credentialsId);
    final ListBoxModel items = new ListBoxModel();
    if (credentials.isPresent()) {
      final RestApiFactory factory = getRestApiFactory(credentials.get());

      try {
        for (final Workspace workspace : WORKSPACES.getWorkspaces(factory)) {
          final String id = workspace.getId();
          final Option option =
            new Option(workspace.getName(), id, Objects.equals(id, workspaceId));
          items.add(option);
        }
      } catch (final IOException e) {
        items.add("Failed to connect to OctoPerf, please check your credentials. "+e);
        e.printStackTrace();
      }
    }

    return getOptions(items);
  }

  public ListBoxModel doFillScenarioIdItems(
    @QueryParameter final String credentialsId,
    @QueryParameter final String workspaceId,
    @QueryParameter final String scenarioId) {
    val credentials = getCredential(credentialsId);

    final ListBoxModel items = new ListBoxModel();
    if (credentials.isPresent() && ofNullable(emptyToNull(workspaceId)).isPresent()) {
      final RestApiFactory apiFactory = getRestApiFactory(credentials.get());

      try {
        final List<Pair<Project, Scenario>> scenariosByProject = SCENARIOS
          .getScenariosByWorkspace(apiFactory, workspaceId);
        for (final Pair<Project, Scenario> entry : scenariosByProject) {
          final Project project = entry.getLeft();
          final Scenario scenario = entry.getRight();
          final String displayName = project.getName() + ARROW + scenario.getName();
          final String id = scenario.getId();
          final Option option = new Option(displayName, id, Objects.equals(id, scenarioId));
          items.add(option);
        }
      } catch (final IOException e) {
        items.add("Failed to connect to OctoPerf, please check your credentials. "+e);
        e.printStackTrace();
      }
    }

    return getOptions(items);
  }

  @NotNull
  private ListBoxModel getOptions(final ListBoxModel items) {
    if (items.isEmpty()) {
      items.add(NONE_DISPLAY_TEXT, NONE_ID);
    }
    return items;
  }


  private static Optional<OctoperfCredential> getCredential(final String credentialsId) {
    if (credentialsId.isEmpty()) {
      return CREDENTIALS_SERVICE.findFirst();
    }
    return CREDENTIALS_SERVICE.find(credentialsId);
  }

  private RestApiFactory getRestApiFactory(final OctoperfCredential credentials) {
    final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(octoperfURL, System.out);
    final RestApiFactory factory = pair.getLeft();

    final String username = credentials.getUsername();
    final String password = credentials.getPassword().getPlainText();
    final RestClientAuthenticator authenticator = pair.getRight();
    authenticator.onUsernameAndPassword(username, password);

    return factory;
  }

  @Override
  public boolean configure(final StaplerRequest req, final JSONObject formData) throws FormException {
    final String octoperfUrl = formData.optString("octoperfURL");
    this.octoperfURL = octoperfUrl.isEmpty() ? DEFAULT_API_URL : octoperfUrl;
    save();
    return true;
  }

  public List<StopConditionDescriptor> getStopConditionDescriptors() {
    return StopConditionDescriptor.all();
  }
}

