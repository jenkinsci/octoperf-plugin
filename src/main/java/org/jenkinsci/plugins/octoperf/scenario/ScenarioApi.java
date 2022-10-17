package org.jenkinsci.plugins.octoperf.scenario;

import org.jenkinsci.plugins.octoperf.report.BenchReport;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ScenarioApi {

  @POST("/runtime/scenarios/run/{id}")
  Call<BenchReport> run(@Path("id") String scenarioId, @Query("name") String name);
  
  @GET("/runtime/scenarios/by-project/{projectId}")
  Call<List<Scenario>> list(@Path("projectId") String projectId);

  @GET("/runtime/scenarios/{id}")
  Call<Scenario> find(@Path("id") String id);
}
