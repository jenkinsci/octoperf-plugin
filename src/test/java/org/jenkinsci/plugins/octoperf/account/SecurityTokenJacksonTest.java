package org.jenkinsci.plugins.octoperf.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link SecurityToken}.
 * 
 * @author jerome
 *
 */
public class SecurityTokenJacksonTest {
  
  private final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final SecurityToken dto = new SecurityToken("tokenKey", DateTime.now());
    
    final String json = mapper.writeValueAsString(dto);
    final SecurityToken fromJson = mapper.readValue(json, SecurityToken.class);
    assertEquals(dto, fromJson);
  }
}
