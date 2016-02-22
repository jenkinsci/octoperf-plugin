package org.jenkinsci.plugins.octoperf.account;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.testing.NullPointerTester;
import com.google.common.testing.NullPointerTester.Visibility;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Tests {@link Credentials}.
 * 
 * @author jerome
 *
 */
public class CredentialsTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(Credentials.class).verify();
  }
  
  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(Credentials.class, Visibility.PACKAGE);
  }
  
  @Test
  public void shouldCreate() {
    final Credentials credentials = new Credentials("token");
    assertEquals("token", credentials.getToken());
  }
  
}
