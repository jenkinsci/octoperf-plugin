package org.jenkinsci.plugins.octoperf.metrics;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.metrics.MetricsService.METRICS;
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
public class MetricsServiceTest {
  
  private static final MetricValues METRIC_VALUES = MetricValuesTest.newMetrics();
  private static final String BENCH_RESULT_ID = "benchResultId";
  @Mock
  RestAdapter adapter;
  @Mock
  MetricsApi api;
  
  @Before
  public void before() {
    when(adapter.create(MetricsApi.class)).thenReturn(api);
    when(api.getMetrics(BENCH_RESULT_ID)).thenReturn(METRIC_VALUES);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RestMetricsService.class, PACKAGE);
  }
  
  @Test
  public void shouldGetMetrics() {
    final MetricValues metrics = METRICS.getMetrics(adapter, BENCH_RESULT_ID);
    assertSame(METRIC_VALUES, metrics);
    verify(adapter).create(MetricsApi.class);
    verify(api).getMetrics(BENCH_RESULT_ID);
  }
}
