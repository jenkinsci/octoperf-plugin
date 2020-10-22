package org.jenkinsci.plugins.octoperf.scenario;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.project.ProjectApi;
import org.jenkinsci.plugins.octoperf.project.ProjectTest;
import org.jenkinsci.plugins.octoperf.report.BenchReport;
import org.jenkinsci.plugins.octoperf.report.BenchReportTest;
import org.jenkinsci.plugins.octoperf.workspace.Workspace;
import org.jenkinsci.plugins.octoperf.workspace.WorkspaceTest;
import org.jenkinsci.plugins.octoperf.workspace.WorkspacesApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.mock.Calls;

import java.io.IOException;
import java.util.List;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ScenarioServiceTest {
  
  private static final Scenario SCENARIO = ScenarioTest.newInstance();
  private static final String SCENARIO_ID = "scenarioId";
  private static final BenchReport BENCH_REPORT = BenchReportTest.newInstance();
  
  @Mock
  RestApiFactory retrofit;
  @Mock
  ScenarioApi api;
  @Mock
  ProjectApi projectApi;
  @Mock
  WorkspacesApi workspacesApi;

  @Before
  public void before() {
    when(retrofit.create(ScenarioApi.class)).thenReturn(api);
    when(retrofit.create(ProjectApi.class)).thenReturn(projectApi);
    when(retrofit.create(WorkspacesApi.class)).thenReturn(workspacesApi);
    final List<Project> projects = ImmutableList.of(ProjectTest.newInstance());
    when(projectApi.getProjects(anyString())).thenReturn(Calls.response(projects));
    final List<Workspace> workspaces = ImmutableList.of(WorkspaceTest.newInstance());
    when(workspacesApi.memberOf()).thenReturn(Calls.response(workspaces));
  }
  
  @Test
  public void shouldRun() throws IOException {
    when(api.run(SCENARIO_ID)).thenReturn(Calls.response(BENCH_REPORT));
    final BenchReport report = SCENARIOS.startTest(retrofit, SCENARIO_ID);
    assertSame(BENCH_REPORT, report);
    verify(api).run(SCENARIO_ID);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RetrofitScenarioService.class, PACKAGE);
  }
  
  @Test
  public void shouldFind() throws IOException{
    when(api.find(SCENARIO.getId())).thenReturn(Calls.response(SCENARIO));
    SCENARIOS.find(retrofit, SCENARIO.getId());
    verify(retrofit).create(ScenarioApi.class);
    verify(api).find(SCENARIO.getId());
  }
  
  @Test
  public void shouldListScenariosPerProject() throws IOException{
    when(api.list(anyString())).thenReturn(Calls.response(ImmutableList.of(SCENARIO)));
    final List<Pair<Project, Scenario>> table = SCENARIOS.getScenariosByWorkspace(retrofit, "workspaceId");
    assertFalse(table.isEmpty());
    verify(workspacesApi).memberOf();
    verify(projectApi).getProjects(anyString());
    verify(retrofit).create(ScenarioApi.class);
    verify(api).list(anyString());
  }
  
}
