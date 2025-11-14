package org.jenkinsci.plugins.octoperf.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
public class LoginFailed implements LoginResult {
  String message;

  @JsonCreator
  public LoginFailed(@JsonProperty("message") final String message) {
    super();
    this.message = requireNonNull(message);
  }
}
