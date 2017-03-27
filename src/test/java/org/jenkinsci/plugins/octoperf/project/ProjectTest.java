package org.jenkinsci.plugins.octoperf.project;

import com.google.common.testing.NullPointerTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link Project}.
 * 
 * @author jerome
 *
 */
public class ProjectTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(Project.class).verify();
  }
  
  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(Project.class, PACKAGE);
  }
  
  @Test
  public void shouldCreate() {
    final Project project = newInstance();
    assertNotNull(project);
  }
  
  public static Project newInstance() {
    return new Project("designProjectId", "resultProjectId", "name");
  }
  
}
