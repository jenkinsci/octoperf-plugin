package org.jenkinsci.plugins.octoperf.project;

import java.util.List;

import retrofit.RestAdapter;

public interface ProjectService {
  
  ProjectService PROJECTS = new RetrofitProjectService();
  
  /**
   * Returns the scenario ids per project id.
   * @param adapter 
   * @return
   */
  List<Project> getProjects(RestAdapter adapter);
}
