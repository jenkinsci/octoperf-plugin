package org.jenkinsci.plugins.octoperf;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.project.ProjectService;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.io.IOException;

import static com.cloudbees.plugins.credentials.CredentialsScope.GLOBAL;
import static org.jenkinsci.plugins.octoperf.OctoperfBuilder.DESCRIPTOR;
import static org.jenkinsci.plugins.octoperf.client.RestClientService.CLIENTS;

/**
 * Username and password to login on Octoperf's platform.
 * 
 * @author jerome
 *
 */
public class OctoperfCredentialImpl extends UsernamePasswordCredentialsImpl implements OctoperfCredential {

  private static final long serialVersionUID = -3802174823289690186L;

  @DataBoundConstructor
  public OctoperfCredentialImpl(
      final CredentialsScope scope, 
      final String id, 
      final String description,
      final String username, 
      final String password) {
    super(scope, id, description, username, password);
  }
  
  @Extension(ordinal = 1)
  public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {
    
    @Override
    public String getDisplayName() {
      return "Octoperf Account";
    }

    @Override
    public ListBoxModel doFillScopeItems() {
      final ListBoxModel m = new ListBoxModel();
      m.add(GLOBAL.getDisplayName(), GLOBAL.toString());
      return m;
    }

    // Used by global.jelly to authenticate User key
    public FormValidation doTestLogin(
        @QueryParameter("username") final String username,
        @QueryParameter("password") final Secret password) throws MessagingException, IOException, ServletException {
      try {
        final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(DESCRIPTOR.getOctoperfURL(), System.out);
        pair.getRight().onUsernameAndPassword(username, password.getPlainText());
        // Get projects to test authentication
        ProjectService.PROJECTS.getProjects(pair.getLeft());
        return FormValidation.ok("Successfully logged in!");
      } catch(final IOException e) {
        return FormValidation.error("Invalid credentials",e);        
      }
    }
  }
}
