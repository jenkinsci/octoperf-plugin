package org.jenkinsci.plugins.octoperf.metrics;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.NullPointerTester.Visibility;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link MetricValue}.
 * 
 * @author jerome
 *
 */
public class MetricValuesTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(MetricValues.class).verify();
  }
  
  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(MetricValues.class, Visibility.PACKAGE);
  }
  
  @Test
  public void shouldCreate() {
    final MetricValues metrics = newMetrics();
    assertNotNull(metrics);
  }
  
  public static MetricValues newMetrics() {
    return new MetricValues(ImmutableList.<MetricValue>of(MetricValueTest.newMetric()));
  }
  
}
