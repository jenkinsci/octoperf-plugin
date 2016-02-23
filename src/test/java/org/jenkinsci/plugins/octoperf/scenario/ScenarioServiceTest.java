package org.jenkinsci.plugins.octoperf.scenario;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jenkinsci.plugins.octoperf.project.Project;
import org.jenkinsci.plugins.octoperf.project.ProjectApi;
import org.jenkinsci.plugins.octoperf.project.ProjectTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.testing.NullPointerTester;

import retrofit.RestAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioServiceTest {
  
  private static final Scenario SCENARIO = ScenarioTest.newInstance();
  
  @Mock
  RestAdapter adapter;
  @Mock
  ScenarioApi api;
  @Mock
  ProjectApi projectApi;
  
  @Before
  public void before() {
    when(adapter.create(ScenarioApi.class)).thenReturn(api);
    when(adapter.create(ProjectApi.class)).thenReturn(projectApi);
    when(projectApi.getProjects()).thenReturn(ImmutableList.of(ProjectTest.newInstance()));
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RetrofitScenarioService.class, PACKAGE);
  }
  
  @Test
  public void shouldFind() {
    when(api.find(SCENARIO.getId())).thenReturn(SCENARIO);
    SCENARIOS.find(adapter, SCENARIO.getId());
    verify(adapter).create(ScenarioApi.class);
    verify(api).find(SCENARIO.getId());
  }
  
  @Test
  public void shouldListScenariosPerProject() {
    when(api.list(SCENARIO.getProjectId())).thenReturn(ImmutableList.of(SCENARIO));
    final Multimap<Project, Scenario> multimap = SCENARIOS.getScenariosByProject(adapter);
    assertFalse(multimap.isEmpty());
    verify(projectApi).getProjects();
    verify(adapter).create(ScenarioApi.class);
    verify(api).list(anyString());
  }
  
}
