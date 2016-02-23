package org.jenkinsci.plugins.octoperf.result;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jenkinsci.plugins.octoperf.result.BenchResult;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests {@link BenchResult}.
 * 
 * @author jerome
 *
 */
public class BenchResultJacksonTest {
  
  private final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final BenchResult dto = BenchResultTest.newInstance();
    
    final String json = mapper.writeValueAsString(dto);
    final BenchResult fromJson = mapper.readValue(json, BenchResult.class);
    assertEquals(dto, fromJson);
  }
}
