package org.jenkinsci.plugins.octoperf.report;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A bench report is attached to a given bench result, 
 * in a given project.
 * 
 * @author jerome
 *
 */
@Value
@JsonIgnoreProperties(ignoreUnknown=true)
public class BenchReport {
  String id;
  String projectId;
  List<String> benchResultIds;
  String name;
  
  @JsonCreator
  BenchReport(
      @JsonProperty("id") final String id,
      @JsonProperty("projectId") final String projectId,
      @JsonProperty("benchResultIds") final List<String> benchResultIds,
      @JsonProperty("name") final String name) {
    super();
    this.id = checkNotNull(id);
    this.projectId = checkNotNull(projectId);
    this.benchResultIds = checkNotNull(benchResultIds);
    this.name = checkNotNull(name);
  }

  @JsonIgnore
  public String getBenchResultId() {
    return benchResultIds.get(0);
  }
}
