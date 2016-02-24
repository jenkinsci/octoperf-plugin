package org.jenkinsci.plugins.octoperf.result;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Provides common operations on {@link BenchResult}.
 * 
 * @author jerome
 *
 */
public interface BenchResultService {
  /**
   * {@link BenchResultService} singleton instance.
   */
  static BenchResultService BENCH_RESULTS = new RestBenchResultService();
  
  /**
   * Finds a bench result by its id.
   * @param id bench result id 
   * @return bench result, when found
   * @throws RetrofitError when not found
   */
  BenchResult find(RestAdapter adapter, String benchResultId);
  
  /**
   * Refreshed the state of the given benchresult.
   * 
   * @param benchResultId bench result id
   * @return
   */
  BenchResultState getState(RestAdapter adapter, String benchResultId);
  
  /**
   * Returns true when the bench result is finished.
   * 
   * @param benchResult bench result
   * @return {@code true} if the result execution is finished
   */
  boolean isFinished(BenchResult benchResult);
}
