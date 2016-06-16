package org.jenkinsci.plugins.octoperf.metrics;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.IOException;

final class RestMetricsService implements MetricsService {

  @Override
  public MetricValues getMetrics(final RestApiFactory apiFactory, final String benchResultId)  throws IOException {
    final MetricsApi api = apiFactory.create(MetricsApi.class);
    return api.getMetrics(benchResultId).execute().body();
  }

  @Override
  public String toPrintable(final DateTime startTime, final MetricValues metrics) {
    final DateTime now = DateTime.now();
    final Duration duration = new Duration(startTime, now);
    
    final StringBuilder b = new StringBuilder(256);
    for(final MetricValue metric : metrics.getMetrics()) {
      b.append(metric.getName()).append(": ").append(String.format("%.2f",metric.getValue())).append(metric.getUnit());
      b.append(" ");
    }
    
    b.append("Duration: ").append(duration.getStandardSeconds() + "s");
    return b.toString();
  }

}
