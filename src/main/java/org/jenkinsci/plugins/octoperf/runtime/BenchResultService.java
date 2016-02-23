package org.jenkinsci.plugins.octoperf.runtime;

import retrofit.RestAdapter;

public interface BenchResultService {
  static BenchResultService BENCH_RESULTS = new RestBenchResultService();
  
  /**
   * Launches the test on Octoperf's load testing platform.
   * 
   * @param adapter rest adapter already logged in
   * @param scenarioId scenario id
   * @return the test currently running
   */
  BenchReport startTest(RestAdapter adapter, String scenarioId);
  
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
