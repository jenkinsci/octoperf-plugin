package org.jenkinsci.plugins.octoperf.metrics;

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
public class MetricValueTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(MetricValue.class).verify();
  }
  
  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(MetricValue.class, Visibility.PACKAGE);
  }
  
  @Test
  public void shouldCreate() {
    final MetricValue metric = newMetric();
    assertNotNull(metric);
  }
  
  public static MetricValue newMetric() {
    return new MetricValue("elapsedTime", 0d, "s");
  }
  
}
