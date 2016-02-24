package org.jenkinsci.plugins.octoperf.metrics;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Single metric value. Example: Avg Elapsedtime: 5.6s.
 * 
 * @author jerome
 *
 */
@Value
public class MetricValue {
  String name;
  double value;
  String unit;
  
  @JsonCreator
  MetricValue(
      @JsonProperty("name") final String name, 
      @JsonProperty("value") final double value, 
      @JsonProperty("unit") final String unit) {
    super();
    this.name = checkNotNull(name);
    this.value = checkNotNull(value);
    this.unit = checkNotNull(unit);
  }
  
}
