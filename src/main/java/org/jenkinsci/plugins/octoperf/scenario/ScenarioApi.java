package org.jenkinsci.plugins.octoperf.scenario;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

interface ScenarioApi {

  @GET("/scenario/list/{projectId}")
  List<Scenario> list(@Path("projectId") String projectId);

  @GET("/scenario/find/{id}")
  Scenario find(@Path("id") String id);
}
