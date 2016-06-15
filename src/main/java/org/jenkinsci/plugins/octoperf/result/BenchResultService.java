package org.jenkinsci.plugins.octoperf.result;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Retrofit;

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
  static BenchResultService BENCH_RESULTS = new RestBenchResultService();

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

  /**
   * Returns true when the bench result is finished.
   *
   * @param benchResult bench result
   * @return {@code true} if the result execution is finished
   */
  boolean isFinished(BenchResult benchResult);
}
