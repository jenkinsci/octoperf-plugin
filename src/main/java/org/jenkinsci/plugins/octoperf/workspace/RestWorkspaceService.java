package org.jenkinsci.plugins.octoperf.workspace;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

final class RestWorkspaceService implements WorkspaceService {

  @Override
  public List<Workspace> getWorkspaces(final RestApiFactory factory) throws IOException {
    final WorkspacesApi api = factory.create(WorkspacesApi.class);
    final Response<List<Workspace>> response = api.memberOf().execute();
    return response.body();
  }
}
