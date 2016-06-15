package org.jenkinsci.plugins.octoperf.project;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;


final class RetrofitProjectService implements ProjectService {

  @Override
  public List<Project> getProjects(final RestApiFactory apiFactory) throws IOException {
    final ProjectApi api = apiFactory.create(ProjectApi.class);
    return api.getProjects().execute().body();
  }

}
