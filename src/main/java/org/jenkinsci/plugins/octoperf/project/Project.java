package org.jenkinsci.plugins.octoperf.project;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;


/**
 * A project encloses virtual users, scenarios and test results.
 * 
 * @author jerome
 *
 */
@Value
@JsonIgnoreProperties(ignoreUnknown=true)
public class Project {
  String id;
  String name;
  
  @JsonCreator
  Project(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name) {
    super();
    this.id = checkNotNull(id);
    this.name = checkNotNull(name);
  }
}
