package org.jenkinsci.plugins.octoperf.metrics;

import com.google.common.testing.NullPointerTester;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.mock.Calls;

import java.io.IOException;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.metrics.MetricsService.METRICS;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MetricsServiceTest {
  
  private static final MetricValues METRIC_VALUES = MetricValuesTest.newMetrics();
  private static final String BENCH_RESULT_ID = "benchResultId";
  @Mock
  RestApiFactory apiFactory;
  @Mock
  MetricsApi api;
  
  @Before
  public void before() {
    when(apiFactory.create(MetricsApi.class)).thenReturn(api);
    when(api.getMetrics(BENCH_RESULT_ID)).thenReturn(Calls.response(METRIC_VALUES));
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RestMetricsService.class, PACKAGE);
  }
  
  @Test
  public void shouldGetMetrics() throws IOException {
    final MetricValues metrics = METRICS.getMetrics(apiFactory, BENCH_RESULT_ID);
    assertSame(METRIC_VALUES, metrics);
    verify(apiFactory).create(MetricsApi.class);
    verify(api).getMetrics(BENCH_RESULT_ID);
  }
}
