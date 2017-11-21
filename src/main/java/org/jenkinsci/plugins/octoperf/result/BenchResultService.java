package org.jenkinsci.plugins.octoperf.result;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;

/**
 * Provides common operations on {@link BenchResult}.
 *
 * @author jerome
 */
public interface BenchResultService {
  /**
   * {@link BenchResultService} singleton instance.
   */
  BenchResultService BENCH_RESULTS = new RestBenchResultService();

  /**
   * Finds a bench result by its id.
   *
   * @param benchResultId bench result id
   * @return bench result, when found
   */
  BenchResult find(RestApiFactory apiFactory, String benchResultId) throws IOException;

  /**
   * Refreshed the state of the given benchresult.
   *
   * @param benchResultId bench result id
   * @return
   */
  BenchResultState getState(RestApiFactory apiFactory, String benchResultId) throws IOException;

  Double getProgress(RestApiFactory apiFactory, String benchResultId) throws IOException;

  /**
   * Returns true when the bench result is finished.
   *
   * @param benchResult bench result
   * @return {@code true} if the result execution is finished
   */
  boolean isFinished(BenchResult benchResult);

  /**
   * Stops the test.
   *
   * @param benchResultId bench result id
   */
  void stopTest(RestApiFactory apiFactory, String benchResultId) throws IOException;
}
