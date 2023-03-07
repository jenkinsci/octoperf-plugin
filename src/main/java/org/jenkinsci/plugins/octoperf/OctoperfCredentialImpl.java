package org.jenkinsci.plugins.octoperf;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import hudson.Extension;
import hudson.model.Item;
import hudson.util.FormValidation;
import hudson.util.Secret;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.jenkinsci.plugins.octoperf.workspace.Workspace;
import org.jenkinsci.plugins.octoperf.workspace.WorkspacesApi;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static hudson.Util.fixEmptyAndTrim;
import static hudson.model.Item.CONFIGURE;
import static hudson.util.FormValidation.error;
import static hudson.util.FormValidation.ok;
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
      return "OctoPerf Account";
    }

    // Used by global.jelly to authenticate User key
    @POST
    public FormValidation doTestLogin(
      @QueryParameter("username") final String username,
      @QueryParameter("password") final Secret password,
      @AncestorInPath final Item item) throws IOException {
      if (item == null) { // no context
        return ok();
      }
      item.checkPermission(CONFIGURE);

      if (fixEmptyAndTrim(username) == null || fixEmptyAndTrim(password.getPlainText()) == null) {
        return error("username and/or password cannot be empty");
      }

      final Pair<RestApiFactory, RestClientAuthenticator> pair = CLIENTS.create(
        OctoperfBuilderDescriptor.getDescriptor().getOctoperfURL(),
        System.out
      );

      pair.getRight().onUsernameAndPassword(username, password.getPlainText());
      // Get projects to test authentication
      final RestApiFactory factory = pair.getLeft();
      final WorkspacesApi api = factory.create(WorkspacesApi.class);
      final Response<List<Workspace>> response = api.memberOf().execute();
      if(response.isSuccessful()) {
        return ok("Successfully logged in!");
      }
      return error(new IOException(response.toString()), "Login failed!");
    }
  }
}
