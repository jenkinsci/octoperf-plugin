package org.jenkinsci.plugins.octoperf.metrics;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MetricsApi {
  
  @GET("/analysis/metrics/global/{benchResultId}")
  Call<MetricValues> getMetrics(@Path("benchResultId") String benchResultId);
}
