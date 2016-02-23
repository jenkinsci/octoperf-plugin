package org.jenkinsci.plugins.octoperf.result;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.result.BenchResultService.BENCH_RESULTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.testing.NullPointerTester;

import retrofit.RestAdapter;

@RunWith(MockitoJUnitRunner.class)
public class BenchResultServiceTest {
  
  private static final BenchResult BENCHRESULT = BenchResultTest.newInstance();
  
  @Mock
  RestAdapter adapter;
  @Mock
  BenchResultApi api;
  
  @Before
  public void before() {
    when(adapter.create(BenchResultApi.class)).thenReturn(api);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RestBenchResultService.class, PACKAGE);
  }
  
  @Test
  public void shouldRefresh() {
    when(api.find(BENCHRESULT.getId())).thenReturn(BENCHRESULT);
    final BenchResultState refreshed = BENCH_RESULTS.getState(adapter, BENCHRESULT.getId());
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
