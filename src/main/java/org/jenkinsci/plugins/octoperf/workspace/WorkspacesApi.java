package org.jenkinsci.plugins.octoperf.workspace;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface WorkspacesApi {

  @GET("/workspaces/member-of")
  Call<List<Workspace>> memberOf();
}
