package org.jenkinsci.plugins.octoperf.report;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;

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
    return new BenchReport("id", "projectId", ImmutableList.of("benchResultId"), "name");
  }
  
}
