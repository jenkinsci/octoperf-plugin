package org.jenkinsci.plugins.octoperf.scenario;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;

import org.jenkinsci.plugins.octoperf.scenario.BenchReport;
import org.junit.Test;

import com.google.common.testing.NullPointerTester;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Tests {@link BenchReport}.
 * 
 * @author jerome
 *
 */
public class BenchReportTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(BenchReport.class).verify();
  }
  
  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(BenchReport.class, PACKAGE);
  }
  
  @Test
  public void shouldCreate() {
    final BenchReport report = newInstance();
    assertNotNull(report);
  }
  
  public static BenchReport newInstance() {
    return new BenchReport("id", "projectId", "benchResultId", "name");
  }
  
}
