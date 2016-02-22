package org.jenkinsci.plugins.octoperf.runtime;

import retrofit.RestAdapter;

final class RestBenchResultService implements BenchResultService {

  @Override
  public BenchResult startTest(final RestAdapter adapter, final String scenarioId) {
    final BenchResultApi api = adapter.create(BenchResultApi.class);
    return api.run(scenarioId);
  }

  @Override
  public BenchResult refresh(final RestAdapter adapter, final BenchResult result) {
    final BenchResultApi api = adapter.create(BenchResultApi.class);
    return api.find(result.getId());
  }

  @Override
  public boolean isFinished(final BenchResult benchResult) {
    final BenchResultState state = benchResult.getState();
    return state.isTerminalState();
  }

}
