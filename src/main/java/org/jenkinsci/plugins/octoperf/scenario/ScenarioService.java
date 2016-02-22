package org.jenkinsci.plugins.octoperf.scenario;

import org.jenkinsci.plugins.octoperf.project.Project;

import com.google.common.collect.Multimap;

import retrofit.RestAdapter;

public interface ScenarioService {
  ScenarioService SCENARIOS = new RetrofitScenarioService();
  
  /**
   * Returns the scenarios per project.
   * 
   * @param adapter 
   * @return
   */
  Multimap<Project, Scenario> getScenariosByProject(RestAdapter adapter);
}
