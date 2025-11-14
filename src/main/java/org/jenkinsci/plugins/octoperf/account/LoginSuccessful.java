package org.jenkinsci.plugins.octoperf.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
public class LoginSuccessful implements LoginResult {
  SecurityToken token;

  @JsonCreator
  public LoginSuccessful(@JsonProperty("token") final SecurityToken token) {
    super();
    this.token = requireNonNull(token);
  }
}