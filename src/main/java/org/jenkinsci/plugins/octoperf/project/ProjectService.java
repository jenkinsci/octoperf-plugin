package org.jenkinsci.plugins.octoperf.project;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;
import java.util.List;

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
   * @param apiFactory
   * @return list of user projects
   */
  List<Project> getProjects(RestApiFactory apiFactory) throws IOException;
}
