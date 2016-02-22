package org.jenkinsci.plugins.octoperf.scenario;

import static org.jenkinsci.plugins.octoperf.project.ProjectService.PROJECTS;

import java.util.List;

import org.jenkinsci.plugins.octoperf.project.Project;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import retrofit.RestAdapter;

final class RetrofitScenarioService implements ScenarioService {

  @Override
  public Multimap<Project, Scenario> getScenariosByProject(final RestAdapter adapter) {

    final ImmutableMultimap.Builder<Project, Scenario> builder = ImmutableMultimap.builder();
    final List<Project> projects = PROJECTS.getProjects(adapter);
    
    final ScenarioApi api = adapter.create(ScenarioApi.class);
    for(final Project project : projects) {
      builder.putAll(project, api.list(project.getId()));
    }
    return builder.build();
  }

}
