package org.jenkinsci.plugins.octoperf.junit;

import hudson.FilePath;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;

public interface JUnitReportService {
  JUnitReportService JUNIT_REPORTS = new RestJUnitReportService();
  
  /**
   * Saves the junit test report on disk.
   * 
   * @param workspace job workspace
   * @param apiFactory rest retrofit
   * @param benchResultId bench result id
   * @return the path where the file is stored
   * @throws InterruptedException 
   * @throws IOException 
   */
  FilePath saveJUnitReport(
      FilePath workspace,
      RestApiFactory apiFactory,
      String benchResultId) throws IOException, InterruptedException;
}
