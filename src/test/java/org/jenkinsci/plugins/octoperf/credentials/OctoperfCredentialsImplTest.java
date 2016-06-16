package org.jenkinsci.plugins.octoperf.credentials;

import static com.cloudbees.plugins.credentials.CredentialsScope.SYSTEM;
import static org.junit.Assert.assertEquals;

import org.jenkinsci.plugins.octoperf.credentials.OctoperfCredential;
import org.jenkinsci.plugins.octoperf.credentials.OctoperfCredentialImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import com.cloudbees.plugins.credentials.SystemCredentialsProvider;

public class OctoperfCredentialsImplTest {

  @Rule 
  public final JenkinsRule jenkins = new JenkinsRule();
  
  private SystemCredentialsProvider credentials;
  
  @Before
  public void before() {
    credentials = SystemCredentialsProvider.getInstance();
  }
  
  @Test
  public void shouldCreate() {
    final OctoperfCredential creds = new OctoperfCredentialImpl(SYSTEM, "id", "description", "foo", "bar");
    credentials.getCredentials().add(creds);
    assertEquals(1, credentials.getCredentials().size());
  }
}
