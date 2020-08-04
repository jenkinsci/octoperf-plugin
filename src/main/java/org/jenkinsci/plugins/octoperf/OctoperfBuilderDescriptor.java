package org.jenkinsci.plugins.octoperf;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.conditions.StopConditionDescriptor;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.scenario.Scenario;
import org.jenkinsci.plugins.octoperf.workspace.Workspace;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.*;

import static java.util.Optional.ofNullable;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;
import static org.jenkinsci.plugins.octoperf.constants.Constants.DEFAULT_API_URL;
import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;

@Extension
@Getter
@Setter
public class OctoperfBuilderDescriptor extends BuildStepDescriptor<Builder> {
  private static final String ARROW = " => ";

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
        final ListBoxModel.Option option =
            new ListBoxModel.Option(c.getUsername(), id, Objects.equals(id, credentialsId));
        ids.add(id);
        items.add(option);
      }
    }

    return items;
  }

  /**
   * Computes the scenarios list when asked by the UI.
   *
   * @param credentialsId credentials id
   * @return combo box model with all scenarios
   */
  public ListBoxModel doFillScenarioIdItems(@QueryParameter("credentialsId") final String credentialsId) {
    final java.util.Optional<OctoperfCredential> optional;
    if (credentialsId.isEmpty()) {
      optional = CREDENTIALS_SERVICE.findFirst();
    } else {
      optional = CREDENTIALS_SERVICE.find(credentialsId);
    }

    final ListBoxModel items = new ListBoxModel();
    if (optional.isPresent()) {
      final OctoperfCredential credentials = optional.get();
      final String username = credentials.getUsername();
      final String password = credentials.getPassword().getPlainText();

      final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(octoperfURL, System.out);
      final RestApiFactory apiFactory = pair.getLeft();
      pair.getRight().onUsernameAndPassword(username, password);

      try {
        final List<Triple<Workspace, Project, Scenario>> scenariosByProject = SCENARIOS.getScenariosByProject(apiFactory);
        for (final Triple<Workspace, Project, Scenario> entry : scenariosByProject) {
          final Workspace workspace = entry.getLeft();
          final Project project = entry.getMiddle();
          final Scenario scenario = entry.getRight();
          final String displayName = workspace.getName() + ARROW + project.getName() + ARROW + scenario.getName();
          items.add(displayName, scenario.getId());
        }
      } catch (IOException e) {
        items.add("Failed to connect to OctoPerf, please check your credentials. "+e);
        e.printStackTrace();
      }
    }
    return items;
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

