package org.jenkinsci.plugins.octoperf.result;

import retrofit.http.GET;
import retrofit.http.Path;

interface BenchResultApi {

  @GET("/bench/result/find/{id}")
  BenchResult find(@Path("id") String id);
}