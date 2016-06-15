package org.jenkinsci.plugins.octoperf.metrics;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Retrieve test metrics from remote servers.
 * 
 * @author jerome
 *
 */
public interface MetricsService {
  /**
   * {@link MetricsService} singleton instance.
   */
  MetricsService METRICS = new RestMetricsService();
  
  /**
   * Retrieves current test metrics from the server.
   * The returned metrics are test-wide metrics.
   * 
   * @param apiFactory rest retrofit
   * @param benchResultId bench result id
   * @return global test metrics.
   */
  MetricValues getMetrics(RestApiFactory apiFactory, String benchResultId) throws IOException;
  
  /**
   * Converts the metrics into a human readable line of values
   * 
   * @param startTime test start time
   * @param metrics metrics to print
   * @return printable output
   */
  String toPrintable(DateTime startTime, MetricValues metrics);
  
}
