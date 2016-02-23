package org.jenkinsci.plugins.octoperf.result;

import retrofit.RestAdapter;

public interface BenchResultService {
  static BenchResultService BENCH_RESULTS = new RestBenchResultService();
  
  /**
   * Finds a bench result by its id.
   * @param id bench result id 
   * @return
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
   * @param benchResult
   * @return
   */
  boolean isFinished(BenchResult benchResult);
}
