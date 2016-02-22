package org.jenkinsci.plugins.octoperf.report;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jenkinsci.plugins.octoperf.runtime.BenchResult;
import org.jenkinsci.plugins.octoperf.runtime.BenchResultTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.testing.NullPointerTester;

import retrofit.RestAdapter;

@RunWith(MockitoJUnitRunner.class)
public class BenchReportServiceTest {
  
  private static final BenchReport REPORT = BenchReportTest.newInstance();
  private static final BenchResult BENCHRESULT = BenchResultTest.newInstance();
  
  @Mock
  RestAdapter adapter;
  @Mock
  BenchReportApi api;
  
  @Before
  public void before() {
    when(adapter.create(BenchReportApi.class)).thenReturn(api);
    when(api.createDefault(BENCHRESULT.getId())).thenReturn(REPORT);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RestBenchReportService.class, PACKAGE);
  }
  
  @Test
  public void shouldGetBenchReports() {
    final BenchReport report = RestBenchReportService.BENCH_REPORTS.createReport(adapter, BENCHRESULT);
    assertNotNull(report);
    verify(adapter).create(BenchReportApi.class);
    verify(api).createDefault(BENCHRESULT.getId());
  }
}
