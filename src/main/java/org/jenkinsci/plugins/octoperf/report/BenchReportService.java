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

  String getReportUrl(
    String serverUrl,
    String workspaceId,
    BenchReport report);
}