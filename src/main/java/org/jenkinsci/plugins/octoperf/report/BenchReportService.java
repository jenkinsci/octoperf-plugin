package org.jenkinsci.plugins.octoperf.report;

import org.jenkinsci.plugins.octoperf.runtime.BenchResult;

import retrofit.RestAdapter;

public interface BenchReportService {

  BenchReportService BENCH_REPORTS = new RestBenchReportService();
  
  /**
   * Creates the default bench report.
   * 
   * @param adapter rest adapter with login configured
   * @param result bench result
   * @return bench report associated to the bench result
   */
  BenchReport createReport(RestAdapter adapter, BenchResult result);
  
  /**
   * Computes the URL which points to the given report.
   * 
   * @param result bench result
   * @param report bench report
   * @return
   */
  String getReportUrl(BenchResult result, BenchReport report);
}
