package org.jenkinsci.plugins.octoperf.threshold;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

interface ThresholdAlarmApi {

  @POST("/analysis/threshold-alarms/{benchResultId}")
  Call<List<ThresholdAlarm>> getAlarms(@Path("benchResultId") final String benchResultId, @Body RequestBody json);
}
