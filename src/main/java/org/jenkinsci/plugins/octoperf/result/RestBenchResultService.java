package org.jenkinsci.plugins.octoperf.result;

import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;

final class RestBenchResultService implements BenchResultService {

  @Override
  public BenchResultState getState(final RestApiFactory apiFactory, final String benchResultId) throws IOException {
    final BenchResult result = find(apiFactory, benchResultId);
    return result.getState();
  }

  @Override
  public BenchResult find(final RestApiFactory apiFactory, final String benchResultId) throws IOException {
    final BenchResultApi api = apiFactory.create(BenchResultApi.class);
    return api.find(benchResultId).execute().body();
  }

  @Override
  public Double getProgress(final RestApiFactory apiFactory, final String benchResultId) throws IOException {
    final BenchResultApi api = apiFactory.create(BenchResultApi.class);
    return api.getProgress(benchResultId).execute().body().getValue();
  }

  @Override
  public boolean isFinished(final BenchResult benchResult) {
    final BenchResultState state = benchResult.getState();
    return state.isTerminalState();
  }

}
