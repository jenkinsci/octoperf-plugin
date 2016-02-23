package org.jenkinsci.plugins.octoperf.runtime;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface BenchResultApi {

  @POST("/api/scenario/run/{id}")
  BenchReport run(@Path("id") String scenarioId);

  @GET("/bench/result/find/{id}")
  BenchResult find(@Path("id") String id);
}