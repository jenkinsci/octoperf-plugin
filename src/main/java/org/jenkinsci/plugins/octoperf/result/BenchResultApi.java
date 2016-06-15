package org.jenkinsci.plugins.octoperf.result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BenchResultApi {

  @GET("/bench/result/find/{id}")
  Call<BenchResult> find(@Path("id") String id);
}