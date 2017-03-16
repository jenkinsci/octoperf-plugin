package org.jenkinsci.plugins.octoperf.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;

import static org.jenkinsci.plugins.octoperf.date.DateService.DATES;
import static org.junit.Assert.assertEquals;

/**
 * Tests {@link SecurityToken}.
 * 
 * @author jerome
 *
 */
public class SecurityTokenJacksonTest {
  
  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.findAndRegisterModules();
  }
  
  @Test
  public void shouldJacksonSerializeCorrectly() throws IOException {
    final DateTime now = DATES.now();
    final SecurityToken dto = new SecurityToken("tokenKey");
    
    final String json = MAPPER.writeValueAsString(dto);
    final SecurityToken fromJson = MAPPER.readValue(json, SecurityToken.class);
    assertEquals(dto, fromJson);
  }
}
