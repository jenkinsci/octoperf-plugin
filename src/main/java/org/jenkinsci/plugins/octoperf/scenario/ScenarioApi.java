package org.jenkinsci.plugins.octoperf.scenario;

import java.util.List;

import org.jenkinsci.plugins.octoperf.report.BenchReport;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScenarioApi {

  @POST("/scenario/run/{id}")
  Call<BenchReport> run(@Path("id") String scenarioId);
  
  @GET("/scenario/list/{projectId}")
  Call<List<Scenario>> list(@Path("projectId") String projectId);

  @GET("/scenario/find/{id}")
  Call<Scenario> find(@Path("id") String id);
}
