package org.jenkinsci.plugins.octoperf.scenario;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;


@Value
@JsonIgnoreProperties(ignoreUnknown=true)
public class Scenario {
  String id;
  final String projectId;
  final String name;
  
  @JsonCreator
  Scenario(
      @JsonProperty("id") final String id,
      @JsonProperty("projectId") final String projectId,
      @JsonProperty("name") final String name) {
    super();
    this.id = checkNotNull(id);
    this.projectId = checkNotNull(projectId);
    this.name = checkNotNull(name);
  }
}
