package org.jenkinsci.plugins.octoperf.runtime;

import retrofit.RestAdapter;

final class RestBenchResultService implements BenchResultService {

  @Override
  public BenchReport startTest(final RestAdapter adapter, final String scenarioId) {
    final BenchResultApi api = adapter.create(BenchResultApi.class);
    return api.run(scenarioId);
  }

  @Override
  public BenchResultState getState(final RestAdapter adapter, final String benchResultId) {
    final BenchResult result = find(adapter, benchResultId);
    return result.getState();
  }
  
  @Override
  public BenchResult find(final RestAdapter adapter, final String benchResultId) {
    final BenchResultApi api = adapter.create(BenchResultApi.class);
    return api.find(benchResultId);
  }

  @Override
  public boolean isFinished(final BenchResult benchResult) {
    final BenchResultState state = benchResult.getState();
    return state.isTerminalState();
  }

}
