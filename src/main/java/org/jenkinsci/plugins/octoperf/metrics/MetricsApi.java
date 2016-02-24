package org.jenkinsci.plugins.octoperf.metrics;

import retrofit.http.GET;
import retrofit.http.Path;

interface MetricsApi {
  
  @GET("/metrics/global/{benchResultId}")
  MetricValues getMetrics(@Path("benchResultId") String benchResultId);
}
