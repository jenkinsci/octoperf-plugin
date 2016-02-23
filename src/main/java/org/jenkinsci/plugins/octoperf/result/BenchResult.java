package org.jenkinsci.plugins.octoperf.result;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;
import lombok.experimental.Wither;

@Value
@JsonIgnoreProperties(ignoreUnknown=true)
public class BenchResult {
  String id;
  String designProjectId;
  long durationInMs;
  @Wither
  BenchResultState state;

  @JsonCreator
  BenchResult(
      @JsonProperty("id") final String id,
      @JsonProperty("designProjectId") final String designProjectId,
      @JsonProperty("durationInMs") final long durationInMs,
      @JsonProperty("state") final BenchResultState state) {
    super();
    this.id = checkNotNull(id);
    this.designProjectId = checkNotNull(designProjectId);
    this.durationInMs = checkNotNull(durationInMs);
    this.state = checkNotNull(state);
  }
}
