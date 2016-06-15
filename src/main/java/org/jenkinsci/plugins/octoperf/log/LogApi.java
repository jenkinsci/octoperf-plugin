package org.jenkinsci.plugins.octoperf.log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Set;

public interface LogApi {

  @GET("/logs/{benchResultId}")
  Call<Set<String>> getFiles(@Path("benchResultId") String benchResultId);
  
  @GET("/logs/{benchResultId}/download")
  Call<ResponseBody> getFile(
      @Path("benchResultId") String benchResultId,
      @Query("filename") String filename);
}
