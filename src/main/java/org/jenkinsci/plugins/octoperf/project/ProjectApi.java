package org.jenkinsci.plugins.octoperf.project;

import com.google.common.annotations.VisibleForTesting;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

@VisibleForTesting
public interface ProjectApi {

  @GET("/design/projects/{workspaceId}/DESIGN")
  Call<List<Project>> getProjects(@Path("workspaceId") String workspaceId);
}
