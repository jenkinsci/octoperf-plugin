package org.jenkinsci.plugins.octoperf.account;

import com.google.common.testing.NullPointerTester;
import com.google.common.testing.NullPointerTester.Visibility;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link SecurityToken}.
 *
 * @author jerome
 */
public class SecurityTokenTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(SecurityToken.class).verify();
  }

  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(SecurityToken.class, Visibility.PACKAGE);
  }

  @Test
  public void shouldCreate() {
    final SecurityToken securityToken = new SecurityToken("refreshToken");
    assertEquals("refreshToken", securityToken.getToken());
  }

}
