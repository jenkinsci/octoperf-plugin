package org.jenkinsci.plugins.octoperf.project;

import java.util.List;

import retrofit.RestAdapter;

final class RetrofitProjectService implements ProjectService {

  @Override
  public List<Project> getProjects(final RestAdapter adapter) {
    final ProjectApi api = adapter.create(ProjectApi.class);
    return api.getProjects();
  }

}
