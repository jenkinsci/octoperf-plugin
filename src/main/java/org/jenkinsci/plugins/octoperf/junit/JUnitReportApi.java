package org.jenkinsci.plugins.octoperf.junit;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public interface JUnitReportApi {

  @GET("/junit/report/{benchResultId}")
  Response getReport(@Path("benchResultId") String benchResultId);
}
