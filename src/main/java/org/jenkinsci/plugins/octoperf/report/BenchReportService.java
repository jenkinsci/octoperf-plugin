package org.jenkinsci.plugins.octoperf.report;

/**
 * Provides common reporting operations.
 *  
 * @author jerome
 *
 */
public interface BenchReportService {
  /**
   * {@link BenchReportService} singleton instance.
   */
  BenchReportService BENCH_REPORTS = new RestBenchReportService();
  
  /**
   * Computes the URL which points to the given report.
   *
   * @param workspaceId workspace id
   * @param report bench report
   * @return
   */
  String getReportUrl(String workspaceId, BenchReport report);
}
