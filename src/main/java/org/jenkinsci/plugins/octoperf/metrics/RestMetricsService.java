package org.jenkinsci.plugins.octoperf.metrics;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import retrofit.RestAdapter;

final class RestMetricsService implements MetricsService {
  
  @Override
  public MetricValues getMetrics(final RestAdapter adapter, final String benchResultId) {
    final MetricsApi api = adapter.create(MetricsApi.class);
    return api.getMetrics(benchResultId);
  }

  @Override
  public String toPrintable(final DateTime startTime, final MetricValues metrics) {
    final DateTime now = DateTime.now();
    final Duration duration = new Duration(startTime, now);
    
    final StringBuilder b = new StringBuilder();
    for(final MetricValue metric : metrics.getMetrics()) {
      b.append(metric.getName() + ": " + String.format("%.2f",metric.getValue()) + metric.getUnit());
      b.append(" ");
    }
    
    b.append("Duration: " + duration.getStandardSeconds() + "s");
    return b.toString();
  }

}
