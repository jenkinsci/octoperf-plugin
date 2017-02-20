package org.jenkinsci.plugins.octoperf.workspace;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static com.google.common.base.Preconditions.checkNotNull;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Workspace {
  String id;
  String name;

  @JsonCreator
  public Workspace(
    @JsonProperty("id") final String id,
    @JsonProperty("name") final String name) {
    this.id = checkNotNull(id);
    this.name = checkNotNull(name);
  }
}
