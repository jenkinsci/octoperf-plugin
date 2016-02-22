package org.jenkinsci.plugins.octoperf.report;

import retrofit.http.POST;
import retrofit.http.Path;

interface BenchReportApi {

  @POST("/bench/report/create/default/{benchResultId}")
  BenchReport createDefault(@Path("benchResultId") String benchResultId);
}
