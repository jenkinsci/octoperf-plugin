package org.jenkinsci.plugins.octoperf.log;

import java.util.Set;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface LogApi {

  @GET("/logs/{benchResultId}")
  Set<String> getFiles(@Path("benchResultId") String benchResultId);
  
  @GET("/logs/{benchResultId}/download")
  Response getFile(
      @Path("benchResultId") String benchResultId,
      @Query("filename") String filename);
}
