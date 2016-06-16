package org.jenkinsci.plugins.octoperf.credentials;

import static com.cloudbees.plugins.credentials.CredentialsScope.SYSTEM;
import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jenkinsci.plugins.octoperf.OctoperfCredential;
import org.jenkinsci.plugins.octoperf.OctoperfCredentialImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;

import hudson.model.Item;

public class JenkinsCredentialsServiceTest {

  private final CredentialsService service = new JenkinsCredentialsService();

  @Rule 
  public final JenkinsRule jenkins = new JenkinsRule();
  
  private SystemCredentialsProvider credentials;
  
  @Before
  public void before() {
    credentials = SystemCredentialsProvider.getInstance();
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(JenkinsCredentialsService.class, PACKAGE);
  }

  @Test
  public void shouldCreateSingleton() {
    assertNotNull(CredentialsService.CREDENTIALS_SERVICE);
  }

  @Test
  public void shouldNotFindFirst() {
    final Optional<OctoperfCredential> first = service.findFirst();
    assertEquals(Optional.absent(), first);
  }

  @Test
  public void shouldFindFirst() {
    final OctoperfCredential creds = new OctoperfCredentialImpl(SYSTEM, "id", "description", "foo", "bar");
    credentials.getCredentials().add(creds);
    
    final Optional<OctoperfCredential> first = service.findFirst();
    assertEquals(Optional.of(creds), first);
  }

  @Test
  public void shouldNotFindById() {
    final Optional<OctoperfCredential> any = service.find("any");
    assertEquals(Optional.absent(), any);
  }

  @Test
  public void shouldFindById() {
    final OctoperfCredential creds = new OctoperfCredentialImpl(SYSTEM, "id", "description", "foo", "bar");

    SystemCredentialsProvider.getInstance().getCredentials().add(creds);
    final Optional<OctoperfCredential> first = service.find(creds.getId());
    assertEquals(Optional.of(creds), first);
  }

  @Test
  public void shouldGetCredentials() {
    final OctoperfCredential creds = new OctoperfCredentialImpl(SYSTEM, "id", "description", "john", "smith");
    credentials.getCredentials().add(creds);
    final OctoperfCredential duplicate = new OctoperfCredentialImpl(SYSTEM, "id", "description", "foo", "bar");
    credentials.getCredentials().add(duplicate);

    final List<OctoperfCredential> all = service.getCredentials(SYSTEM, Optional.<Item> absent());
    assertEquals(ImmutableList.of(creds), all);
  }
}
