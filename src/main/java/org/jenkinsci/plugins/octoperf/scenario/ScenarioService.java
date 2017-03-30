package org.jenkinsci.plugins.octoperf.scenario;

import org.apache.commons.lang3.tuple.Triple;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.report.BenchReport;
import org.jenkinsci.plugins.octoperf.workspace.Workspace;

import java.io.IOException;
import java.util.List;

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
   * @param apiFactory rest adapter already logged in
   * @param scenarioId scenario id
   * @return the test currently running
   */
  BenchReport startTest(RestApiFactory apiFactory, String scenarioId) throws IOException;
  
  /**
   * Finds a single scenario by id.
   * 
   * @param apiFactory rest adapter
   * @param id scenario id
   * @return the scenario when found
   */
  Scenario find(RestApiFactory apiFactory, String id) throws IOException;
  
  /**
   * Returns the scenarios per project.
   * 
   * @param apiFactory rest adapter
   * @return project to scenarios multimap
   */
  List<Triple<Workspace, Project, Scenario>> getScenariosByProject(RestApiFactory apiFactory) throws IOException;
}
