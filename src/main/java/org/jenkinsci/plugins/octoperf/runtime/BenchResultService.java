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
  BenchResult startTest(RestAdapter adapter, String scenarioId);
  
  /**
   * Refreshed the state of the given benchresult.
   * 
   * @param result
   * @return
   */
  BenchResult refresh(RestAdapter adapter, BenchResult result);
  
  /**
   * Returns true when the bench result is finished.
   * 
   * @param benchResult
   * @return
   */
  boolean isFinished(BenchResult benchResult);
}
