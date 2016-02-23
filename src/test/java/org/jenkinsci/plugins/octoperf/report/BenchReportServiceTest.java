package org.jenkinsci.plugins.octoperf.report;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.testing.NullPointerTester;

@RunWith(MockitoJUnitRunner.class)
public class BenchReportServiceTest {
  
  private static final BenchReport REPORT = BenchReportTest.newInstance();
  
  @Before
  public void before() {
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RestBenchReportService.class, PACKAGE);
  }
  
  @Test
  public void shouldGetBenchReports() {
    final String reportUrl = RestBenchReportService.BENCH_REPORTS.getReportUrl(REPORT);
    assertEquals("https://app.octoperf.com/#/app/project/projectId/analysis/benchResultId/id", reportUrl);
  }
}
