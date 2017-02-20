package org.jenkinsci.plugins.octoperf.workspace;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;
import java.util.List;

public interface WorkspaceService {
  WorkspaceService WORKSPACES = new RestWorkspaceService();

  List<Workspace> getWorkspaces(RestApiFactory factory) throws IOException;
}
