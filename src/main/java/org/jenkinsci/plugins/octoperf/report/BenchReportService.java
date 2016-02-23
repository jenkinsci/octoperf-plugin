package org.jenkinsci.plugins.octoperf.report;

import org.jenkinsci.plugins.octoperf.scenario.BenchReport;

public interface BenchReportService {

  BenchReportService BENCH_REPORTS = new RestBenchReportService();
  
  /**
   * Computes the URL which points to the given report.
   * 
   * @param result bench result
   * @param report bench report
   * @return
   */
  String getReportUrl(BenchReport report);
}
