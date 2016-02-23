package org.jenkinsci.plugins.octoperf.scenario;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.testing.NullPointerTester;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Tests {@link Scenario}.
 * 
 * @author jerome
 *
 */
public class ScenarioTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(Scenario.class).verify();
  }
  
  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(Scenario.class, PACKAGE);
  }
  
  @Test
  public void shouldCreate() {
    final Scenario scenario = newInstance();
    assertNotNull(scenario);
  }
  
  public static Scenario newInstance() {
    return new Scenario("id", "projectId", "name", "description");
  }
  
}
