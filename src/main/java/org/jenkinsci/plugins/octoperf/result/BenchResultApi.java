package org.jenkinsci.plugins.octoperf.result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BenchResultApi {

  @GET("/runtime/bench-results/{id}")
  Call<BenchResult> find(@Path("id") String id);

  @GET("/runtime/bench-results/progress/{benchResultId}")
  Call<ValueWrapper<Double>> getProgress(@Path("benchResultId") String benchResultId);
}