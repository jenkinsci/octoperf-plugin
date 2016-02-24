package org.jenkinsci.plugins.octoperf;

import static org.jenkinsci.plugins.octoperf.Constants.DEFAULT_API_URL;
import static org.jenkinsci.plugins.octoperf.account.AccountService.ACCOUNTS;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;
import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.scenario.Scenario;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;

import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;
import net.sf.json.JSONObject;
import retrofit.RestAdapter;

public class OctoperfBuilderDescriptor extends BuildStepDescriptor<Builder> {
  private static final String ARROW = " => ";
  private static final String DISPLAY_NAME = "Octoperf";

  private String octoperfURL = Constants.DEFAULT_API_URL;
  private String name = "My Octperf Account";

  OctoperfBuilderDescriptor() {
    super(OctoperfBuilder.class);
    load();
  }

  @Override
  public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
    return true;
  }

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  public ListBoxModel doFillCredentialsIdItems(final Object scope) {
    final ListBoxModel items = new ListBoxModel();
    final Set<String> ids = new HashSet<String>();

    final Item item = Stapler.getCurrentRequest().findAncestorObject(Item.class);
    boolean isFirst = true;
    for (final OctoperfCredential c : CREDENTIALS_SERVICE.getCredentials(scope, Optional.fromNullable(item))) {
      final String id = c.getId();
      if (!ids.contains(id)) {
        final ListBoxModel.Option option = 
            new ListBoxModel.Option(c.getUsername(), id, isFirst);
        ids.add(id);
        items.add(option);
        isFirst = false;
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
  public ListBoxModel doFillScenarioIdItems(final @QueryParameter("credentialsId") String credentialsId) {
    final Optional<OctoperfCredential> optional;
    if(credentialsId.isEmpty()) {
      optional = CREDENTIALS_SERVICE.findFirst();
    } else {
      optional = CREDENTIALS_SERVICE.find(credentialsId);
    }
    
    final ListBoxModel items = new ListBoxModel();
    if(optional.isPresent()) {
      final OctoperfCredential credentials = optional.get();
      final String username = credentials.getUsername();
      final String password = credentials.getPassword().getPlainText();
      
      final Pair<RestAdapter, RestClientAuthenticator> pair = CLIENTS.create(octoperfURL);
      final RestAdapter adapter = ACCOUNTS.login(pair, username, password);
      
      final Multimap<Project, Scenario> scenariosByProject = SCENARIOS.getScenariosByProject(adapter);
      for(final Entry<Project, Scenario> entry : scenariosByProject.entries()) {
        final Project project = entry.getKey();
        final Scenario scenario = entry.getValue();
        final String displayName = project.getName() + ARROW + scenario.getName();
        items.add(displayName, scenario.getId());
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
  
  public String getOctoperfURL() {
    return octoperfURL;
  }
  
  public void setOctoperfURL(String octoperfURL) {
    this.octoperfURL = octoperfURL;
  }

  public String getName() {
    return name;
  }

}

