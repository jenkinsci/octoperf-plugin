package org.jenkinsci.plugins.octoperf.scenario;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.report.BenchReport;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Provides common operations on scenarios.
 * 
 * @author jerome
 *
 */
public interface ScenarioService {
  ScenarioService SCENARIOS = new RetrofitScenarioService();
  
  BenchReport startTest(RestApiFactory apiFactory,
                        String scenarioId,
                        Optional<String> testName) throws IOException;
  
  Scenario find(RestApiFactory apiFactory, String id) throws IOException;
  
  List<Scenario> getScenariosByProject(
    RestApiFactory apiFactory,
    String workspaceId) throws IOException;
}
