package org.jenkinsci.plugins.octoperf.account;

import com.google.common.testing.NullPointerTester;
import com.google.common.testing.NullPointerTester.Visibility;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.DateTime;
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
    final DateTime now = DateTime.now();
    final SecurityToken securityToken = new SecurityToken("refreshToken", now);
    assertEquals("refreshToken", securityToken.getToken());
    assertEquals(now, securityToken.getExpiresAt());
  }

}
