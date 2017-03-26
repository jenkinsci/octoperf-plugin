package org.jenkinsci.plugins.octoperf.report;

import com.google.common.testing.NullPointerTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertEquals;

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
    final String reportUrl = RestBenchReportService.BENCH_REPORTS.getReportUrl("workspacId", REPORT);
    assertEquals("https://app.octoperf.com/#/app/workspace/workspacId/project/projectId/analysis/benchResultId/id", reportUrl);
  }
}
