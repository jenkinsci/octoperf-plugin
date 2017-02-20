package org.jenkinsci.plugins.octoperf.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.joda.time.DateTime;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The database Id is used as the refreshToken
 * 
 * @author gerald pereira
 *
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityToken {
  String token;
  DateTime expiresAt;

  @JsonCreator
  public SecurityToken(
      @JsonProperty("token") final String token,
      @JsonProperty("expiresAt") final DateTime expiresAt) {
    super();
    this.token = checkNotNull(token);
    this.expiresAt = checkNotNull(expiresAt);
  }
  
}
