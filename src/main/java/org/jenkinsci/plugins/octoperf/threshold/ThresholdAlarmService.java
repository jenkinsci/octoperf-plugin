package org.jenkinsci.plugins.octoperf.threshold;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;

public interface ThresholdAlarmService {
  /**
   * {@link ThresholdAlarmService} singleton instance.
   */
  ThresholdAlarmService THRESHOLD_ALARMS = new RetrofitThresholdAlarmService();

  boolean hasAlarms(
    RestApiFactory apiFactory,
    String benchResultId,
    ThresholdSeverity severity) throws IOException;
}
