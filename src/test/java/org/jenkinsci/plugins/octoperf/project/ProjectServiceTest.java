package org.jenkinsci.plugins.octoperf.project;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit2.mock.Calls;

import java.io.IOException;
import java.util.List;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.project.ProjectService.PROJECTS;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
  
  @Mock
  RestApiFactory apiFactory;
  @Mock
  ProjectApi api;
  
  @Before
  public void before() {
    when(apiFactory.create(ProjectApi.class)).thenReturn(api);
    when(api.getProjects()).thenReturn(Calls.response((List<Project>) ImmutableList.of(ProjectTest.newInstance())));
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RetrofitProjectService.class, PACKAGE);
  }
  
  @Test
  public void shouldGetProjects() throws IOException{
    final List<Project> projects = PROJECTS.getProjects(apiFactory);
    assertNotNull(projects);
    verify(apiFactory).create(ProjectApi.class);
    verify(api).getProjects();
  }
}
