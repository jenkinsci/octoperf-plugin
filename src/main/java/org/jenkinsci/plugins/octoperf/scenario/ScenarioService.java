package org.jenkinsci.plugins.octoperf.scenario;

import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.report.BenchReport;

import com.google.common.collect.Multimap;

import retrofit.RestAdapter;

/**
 * Provides common operations on scenarios.
 * 
 * @author jerome
 *
 */
public interface ScenarioService {
  /**
   * {@link ScenarioService} singleton instance.
   */
  ScenarioService SCENARIOS = new RetrofitScenarioService();
  
  /**
   * Launches the test on Octoperf's load testing platform.
   * 
   * @param adapter rest adapter already logged in
   * @param scenarioId scenario id
   * @return the test currently running
   */
  BenchReport startTest(RestAdapter adapter, String scenarioId);
  
  /**
   * Finds a single scenario by id.
   * 
   * @param adapter rest adapter
   * @param id scenario id
   * @return the scenario when found
   */
  Scenario find(RestAdapter adapter, String id);
  
  /**
   * Returns the scenarios per project.
   * 
   * @param adapter rest adapter
   * @return project to scenarios multimap
   */
  Multimap<Project, Scenario> getScenariosByProject(RestAdapter adapter);
}
