package org.jenkinsci.plugins.octoperf.metrics;

import org.joda.time.DateTime;

import retrofit.RestAdapter;

public interface MetricsService {
  MetricsService METRICS = new RestMetricsService();
  
  /**
   * Retrieves current test metrics from the server.
   * The returned metrics are test-wide metrics.
   * 
   * @param adapter rest adapter
   * @param benchResultId bench result id
   * @return global test metrics.
   */
  MetricValues getMetrics(RestAdapter adapter, String benchResultId);
  
  /**
   * Converts the metrics into a human readable line of values 
   * @param metrics
   * @return
   */
  String toPrintable(DateTime startTime, MetricValues metrics);
  
}
