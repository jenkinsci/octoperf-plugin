package org.jenkinsci.plugins.octoperf.scenario;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.report.BenchReport;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

import static org.jenkinsci.plugins.octoperf.project.ProjectService.PROJECTS;

final class RetrofitScenarioService implements ScenarioService {


  @Override
  public BenchReport startTest(final RestApiFactory apiFactory, final String scenarioId) throws IOException {
    final ScenarioApi api = apiFactory.create(ScenarioApi.class);
    return api.run(scenarioId).execute().body();
  }
  
  @Override
  public Scenario find(final RestApiFactory apiFactory, final String id) throws IOException{
    final ScenarioApi api = apiFactory.create(ScenarioApi.class);
    return api.find(id).execute().body();
  }
  
  @Override
  public Multimap<Project, Scenario> getScenariosByProject(final RestApiFactory apiFactory) throws IOException{

    final ImmutableMultimap.Builder<Project, Scenario> builder = ImmutableMultimap.builder();
    final List<Project> projects = PROJECTS.getProjects(apiFactory);
    
    final ScenarioApi api = apiFactory.create(ScenarioApi.class);
    for(final Project project : projects) {
      builder.putAll(project, api.list(project.getId()).execute().body());
    }
    return builder.build();
  }

}
