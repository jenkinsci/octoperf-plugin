package org.jenkinsci.plugins.octoperf.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import lombok.experimental.Wither;

import static com.google.common.base.Preconditions.checkNotNull;

@Value
@JsonIgnoreProperties(ignoreUnknown=true)
public class BenchResult {
  String id;
  String designProjectId;
  @Wither
  BenchResultState state;

  @JsonCreator
  BenchResult(
      @JsonProperty("id") final String id,
      @JsonProperty("designProjectId") final String designProjectId,
      @JsonProperty("state") final BenchResultState state) {
    super();
    this.id = checkNotNull(id);
    this.designProjectId = checkNotNull(designProjectId);
    this.state = checkNotNull(state);
  }
}
