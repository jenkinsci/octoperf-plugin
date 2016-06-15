package org.jenkinsci.plugins.octoperf.account;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests {@link Credentials}.
 * 
 * @author jerome
 *
 */
public class CredentialsJacksonTest {
  
  private final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final Credentials dto = new Credentials("tokenKey", "userId");
    
    final String json = mapper.writeValueAsString(dto);
    final Credentials fromJson = mapper.readValue(json, Credentials.class);
    assertEquals(dto, fromJson);
  }
}
