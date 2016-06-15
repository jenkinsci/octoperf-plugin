package org.jenkinsci.plugins.octoperf.junit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JUnitReportApi {

  @GET("/junit/report/{benchResultId}")
  Call<ResponseBody> getReport(@Path("benchResultId") String benchResultId);
}
