package org.jenkinsci.plugins.octoperf.project;

import java.io.IOException;
import java.util.List;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Retrofit;

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
