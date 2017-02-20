package org.jenkinsci.plugins.octoperf.scenario;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.report.BenchReport;
import org.jenkinsci.plugins.octoperf.workspace.Workspace;
import org.jenkinsci.plugins.octoperf.workspace.WorkspaceService;

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
  public Table<Workspace, Project, Scenario> getScenariosByProject(final RestApiFactory apiFactory) throws IOException{

    final ImmutableTable.Builder<Workspace, Project, Scenario> builder = ImmutableTable.builder();
    final List<Workspace> workspaces = WorkspaceService.WORKSPACES.getWorkspaces(apiFactory);
    for (final Workspace workspace : workspaces) {
      final List<Project> projects = PROJECTS.getProjects(apiFactory, workspace.getId());

      final ScenarioApi api = apiFactory.create(ScenarioApi.class);
      for (final Project project : projects) {
        final List<Scenario> scenarios = api.list(project.getId()).execute().body();
        for (final Scenario scenario : scenarios) {
          builder.put(workspace, project, scenario);
        }
      }
    }
    return builder.build();
  }

}
