package org.jenkinsci.plugins.octoperf.metrics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

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
