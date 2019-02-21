package org.jenkinsci.plugins.octoperf.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link MetricValue}.
 * 
 * @author jerome
 *
 */
public class MetricValueJacksonTest {
  
  private final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final MetricValue dto = MetricValueTest.newMetric();
    
    final String json = mapper.writeValueAsString(dto);
    final MetricValue fromJson = mapper.readValue(json, MetricValue.class);
    assertEquals(dto, fromJson);
  }
}
