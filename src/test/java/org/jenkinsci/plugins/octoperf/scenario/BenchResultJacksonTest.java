package org.jenkinsci.plugins.octoperf.scenario;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests {@link scenario}.
 * 
 * @author jerome
 *
 */
public class BenchResultJacksonTest {
  
  private final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final Scenario dto = ScenarioTest.newInstance();
    
    final String json = mapper.writeValueAsString(dto);
    final Scenario fromJson = mapper.readValue(json, Scenario.class);
    assertEquals(dto, fromJson);
  }
}
