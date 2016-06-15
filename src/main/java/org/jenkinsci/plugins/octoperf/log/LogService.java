package org.jenkinsci.plugins.octoperf.log;

import hudson.FilePath;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Retrofit;

import java.io.IOException;
import java.io.PrintStream;

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
   * @param apiFactory rest retrofit
   * @param benchResultId bench result id
   * @throws IOException when logs could not be written to disk
   */
  void downloadLogFiles(
      FilePath workspace,
      PrintStream logger,
      RestApiFactory apiFactory,
      String benchResultId) throws IOException, InterruptedException;
}
