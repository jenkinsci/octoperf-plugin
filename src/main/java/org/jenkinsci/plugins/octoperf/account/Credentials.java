package org.jenkinsci.plugins.octoperf.account;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Represents an authentication token.
 * This token is sent in each request to stay logged in.
 * 
 * @author jerome
 *
 */
@Value
public class Credentials {
  String token;

  @JsonCreator
  Credentials(@JsonProperty("token") final String token) {
    super();
    this.token = checkNotNull(token);
  }
}
