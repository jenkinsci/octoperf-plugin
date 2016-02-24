package org.jenkinsci.plugins.octoperf.log;

import java.io.IOException;
import java.io.PrintStream;

import hudson.FilePath;
import retrofit.RestAdapter;

public interface LogService {
  /**
   * Singleton {@link LogService} instance.
   */
  LogService LOGS = new JMeterLogService();
  
  /**
   * Downloads the test log files from Octoperf servers once the 
   * test is finished.
   * 
   * @param workspace workspace folder
   * @param logger logs messages
   * @param adapter rest adapter
   * @param benchResultId bench result id
   * @throws IOException when logs could not be written to disk
   */
  void downloadLogFiles(
      FilePath workspace,
      PrintStream logger,
      RestAdapter adapter, 
      String benchResultId) throws IOException, InterruptedException;
}
