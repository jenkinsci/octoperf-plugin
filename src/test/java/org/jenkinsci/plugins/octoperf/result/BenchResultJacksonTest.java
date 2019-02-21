package org.jenkinsci.plugins.octoperf.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
