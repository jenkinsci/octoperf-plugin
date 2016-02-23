package org.jenkinsci.plugins.octoperf.report;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown=true)
public class BenchReport {
  String id;
  String projectId;
  String benchResultId;
  String name;
  
  @JsonCreator
  BenchReport(
      @JsonProperty("id") final String id,
      @JsonProperty("projectId") final String projectId,
      @JsonProperty("benchResultId") final String benchResultId,
      @JsonProperty("name") final String name) {
    super();
    this.id = checkNotNull(id);
    this.projectId = checkNotNull(projectId);
    this.benchResultId = checkNotNull(benchResultId);
    this.name = checkNotNull(name);
  }
    
}
