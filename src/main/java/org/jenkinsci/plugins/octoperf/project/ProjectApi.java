package org.jenkinsci.plugins.octoperf.project;

import java.util.List;

import com.google.common.annotations.VisibleForTesting;

import retrofit.http.GET;

@VisibleForTesting
public interface ProjectApi {

  @GET("/project/list/DESIGN")
  List<Project> getProjects();
}
