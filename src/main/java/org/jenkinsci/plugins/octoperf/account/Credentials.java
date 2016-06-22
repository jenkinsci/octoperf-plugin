package org.jenkinsci.plugins.octoperf.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The database Id is used as the token
 * 
 * @author gerald pereira
 *
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Credentials {
  String id;
  String userId;

  @JsonCreator
  public Credentials(
      @JsonProperty("id") final String id,
      @JsonProperty("userId") final String userId) {
    super();
    this.id = checkNotNull(id);
    this.userId = checkNotNull(userId);
  }
  
}
