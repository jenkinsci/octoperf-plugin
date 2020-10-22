package org.jenkinsci.plugins.octoperf.scenario;

import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.report.BenchReport;

import java.io.IOException;
import java.util.List;

/**
 * Provides common operations on scenarios.
 * 
 * @author jerome
 *
 */
public interface ScenarioService {
  ScenarioService SCENARIOS = new RetrofitScenarioService();
  
  BenchReport startTest(RestApiFactory apiFactory, String scenarioId) throws IOException;
  
  Scenario find(RestApiFactory apiFactory, String id) throws IOException;
  
  List<Pair<Project, Scenario>> getScenariosByWorkspace(
    RestApiFactory apiFactory,
    String workspaceId) throws IOException;
}
