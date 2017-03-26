package org.jenkinsci.plugins.octoperf.project;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;


final class RetrofitProjectService implements ProjectService {

  @Override
  public List<Project> getProjects(final RestApiFactory apiFactory, final String workspaceId) throws IOException {
    final ProjectApi api = apiFactory.create(ProjectApi.class);
    final Response<List<Project>> response = api.getProjects(workspaceId).execute();
    return response.body();
  }

  @Override
  public Project find(final RestApiFactory apiFactory, final String id) throws IOException {
    final ProjectApi api = apiFactory.create(ProjectApi.class);
    final Response<Project> response = api.find(id).execute();
    return response.body();
  }

}
