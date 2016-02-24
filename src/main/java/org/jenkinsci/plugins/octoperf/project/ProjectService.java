package org.jenkinsci.plugins.octoperf.project;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Provides common operations on projects. 
 * 
 * @author jerome
 *
 */
public interface ProjectService {
  
  /**
   * {@link ProjectService} singleton instance.
   */
  ProjectService PROJECTS = new RetrofitProjectService();
  
  /**
   * Returns the scenario ids per project id.
   * @param adapter rest adapter with login performed
   * @return list of user projects
   */
  List<Project> getProjects(RestAdapter adapter);
}
