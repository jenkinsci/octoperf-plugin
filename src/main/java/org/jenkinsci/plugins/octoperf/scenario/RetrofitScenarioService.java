package org.jenkinsci.plugins.octoperf.scenario;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.report.BenchReport;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

final class RetrofitScenarioService implements ScenarioService {


  @Override
  public BenchReport startTest(
    final RestApiFactory apiFactory,
    final String scenarioId,
    final Optional<String> name) throws IOException {
    final ScenarioApi api = apiFactory.create(ScenarioApi.class);
    return api.run(
      scenarioId,
      name.map(Strings::emptyToNull).orElse(null)
    ).execute().body();
  }

  @Override
  public Scenario find(final RestApiFactory apiFactory, final String id) throws IOException{
    final ScenarioApi api = apiFactory.create(ScenarioApi.class);
    return api.find(id).execute().body();
  }

  @Override
  public List<Scenario> getScenariosByProject(
    final RestApiFactory apiFactory,
    final String projectId) throws IOException{
    final ImmutableList.Builder<Scenario> builder = ImmutableList.builder();

    final ScenarioApi api = apiFactory.create(ScenarioApi.class);
    final List<Scenario> scenarios = api.list(projectId).execute().body();
    for (final Scenario scenario : scenarios) {
      builder.add(scenario);
    }

    return builder.build();
  }

}
