package org.jenkinsci.plugins.octoperf.project;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.project.ProjectService.PROJECTS;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;

import retrofit.RestAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
  
  @Mock
  RestAdapter adapter;
  @Mock
  ProjectApi api;
  
  @Before
  public void before() {
    when(adapter.create(ProjectApi.class)).thenReturn(api);
    when(api.getProjects()).thenReturn(ImmutableList.of(ProjectTest.newInstance()));
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RetrofitProjectService.class, PACKAGE);
  }
  
  @Test
  public void shouldGetProjects() {
    final List<Project> projects = PROJECTS.getProjects(adapter);
    assertNotNull(projects);
    verify(adapter).create(ProjectApi.class);
    verify(api).getProjects();
  }
}
