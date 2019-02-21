package org.jenkinsci.plugins.octoperf.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link MetricValues}.
 * 
 * @author jerome
 *
 */
public class MetricValuesJacksonTest {
  
  private final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final MetricValues dto = MetricValuesTest.newMetrics();
    
    final String json = mapper.writeValueAsString(dto);
    final MetricValues fromJson = mapper.readValue(json, MetricValues.class);
    assertEquals(dto, fromJson);
  }
}
