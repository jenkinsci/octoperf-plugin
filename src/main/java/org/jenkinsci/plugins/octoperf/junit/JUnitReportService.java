package org.jenkinsci.plugins.octoperf.junit;

import java.io.IOException;

import hudson.FilePath;
import retrofit.RestAdapter;

public interface JUnitReportService {
  JUnitReportService JUNIT_REPORTS = new RestJUnitReportService();
  
  /**
   * Saves the junit test report on disk.
   * 
   * @param workspace job workspace
   * @param adapter rest adapter
   * @param benchResultId bench result id
   * @return the path where the file is stored
   * @throws InterruptedException 
   * @throws IOException 
   */
  FilePath saveJUnitReport(
      FilePath workspace,
      RestAdapter adapter, 
      String benchResultId) throws IOException, InterruptedException;
}
