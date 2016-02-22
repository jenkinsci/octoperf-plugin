package org.jenkinsci.plugins.octoperf.metrics;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class MetricValues {
  List<MetricValue> metrics;
  
  @JsonCreator
  MetricValues(@JsonProperty("metrics") final List<MetricValue> metrics) {
    this.metrics = checkNotNull(metrics);
  }
}
