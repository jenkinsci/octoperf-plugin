package org.jenkinsci.plugins.octoperf.threshold;


import okhttp3.RequestBody;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;

import static okhttp3.MediaType.parse;
import static okhttp3.RequestBody.create;

final class RetrofitThresholdAlarmService implements ThresholdAlarmService {
  private static final String THRESHOLD_REPORT_ITEM =
    "{\"@type\":\"ThresholdAlarmReportItem\",\"metric\":{\"@type\":\"MonitoringMetric\",\"filters\":[],\"id\":\"\",\"type\":\"NUMBER_COUNTER\"},\"name\":\"\"}";
  private static final RequestBody JSON = create(parse("application/json"), THRESHOLD_REPORT_ITEM);

  public boolean hasAlarms(
    final RestApiFactory apiFactory,
    final String benchResultId,
    final ThresholdSeverity severity) throws IOException {
    final ThresholdAlarmApi api = apiFactory.create(ThresholdAlarmApi.class);

    return api
      .getAlarms(benchResultId, JSON)
      .execute()
      .body()
      .stream()
      .map(ThresholdAlarm::getSeverity)
      .anyMatch(severity::equals);
  }
}
