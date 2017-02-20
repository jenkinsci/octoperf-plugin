package org.jenkinsci.plugins.octoperf.workspace;

import com.google.common.testing.NullPointerTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.jenkinsci.plugins.octoperf.scenario.Scenario;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link Scenario}.
 * 
 * @author jerome
 *
 */
public class WorkspaceTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(Workspace.class).verify();
  }

  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(Workspace.class, PACKAGE);
  }

  @Test
  public void shouldCreate() {
    final Workspace scenario = newInstance();
    assertNotNull(scenario);
  }

  public static Workspace newInstance() {
    return new Workspace("id", "name");
  }

}
