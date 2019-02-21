package org.jenkinsci.plugins.octoperf.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link Project}.
 * 
 * @author jerome
 *
 */
public class ProjectJacksonTest {
  
  private final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final Project dto = ProjectTest.newInstance();
    
    final String json = mapper.writeValueAsString(dto);
    final Project fromJson = mapper.readValue(json, Project.class);
    assertEquals(dto, fromJson);
  }
}
