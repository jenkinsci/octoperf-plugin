package org.jenkinsci.plugins.octoperf.result;

import com.google.common.testing.NullPointerTester;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit2.mock.Calls;

import java.io.IOException;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.result.BenchResultService.BENCH_RESULTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BenchResultServiceTest {
  
  private static final BenchResult BENCHRESULT = BenchResultTest.newInstance();
  
  @Mock
  RestApiFactory retrofit;
  @Mock
  BenchResultApi api;
  
  @Before
  public void before() {
    when(retrofit.create(BenchResultApi.class)).thenReturn(api);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RestBenchResultService.class, PACKAGE);
  }
  
  @Test
  public void shouldRefresh() throws IOException{
    when(api.find(BENCHRESULT.getId())).thenReturn(Calls.response(BENCHRESULT));
    final BenchResultState refreshed = BENCH_RESULTS.getState(retrofit, BENCHRESULT.getId());
    assertSame(BENCHRESULT.getState(), refreshed);
    verify(api).find(BENCHRESULT.getId());
  }
  
  @Test
  public void shouldPassIsFinished() {
    for(final BenchResultState state : BenchResultState.values()) {
      assertEquals(state.isTerminalState(), BENCH_RESULTS.isFinished(BENCHRESULT.withState(state)));
    }
  }
}
