package org.jenkinsci.plugins.octoperf.account;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class Credentials {
  String token;

  @JsonCreator
  Credentials(@JsonProperty("token") final String token) {
    super();
    this.token = checkNotNull(token);
  }
}
