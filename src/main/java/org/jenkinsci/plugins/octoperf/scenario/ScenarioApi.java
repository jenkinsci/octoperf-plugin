package org.jenkinsci.plugins.octoperf.scenario;

import java.util.List;

import org.jenkinsci.plugins.octoperf.report.BenchReport;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

interface ScenarioApi {

  @POST("/scenario/run/{id}")
  BenchReport run(@Path("id") String scenarioId);
  
  @GET("/scenario/list/{projectId}")
  List<Scenario> list(@Path("projectId") String projectId);

  @GET("/scenario/find/{id}")
  Scenario find(@Path("id") String id);
}
