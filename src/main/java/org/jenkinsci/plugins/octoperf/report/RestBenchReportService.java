package org.jenkinsci.plugins.octoperf.report;

import org.jenkinsci.plugins.octoperf.scenario.BenchReport;

final class RestBenchReportService implements BenchReportService {
  
  private static final String REPORT_URL = "https://app.octoperf.com/#/app/project/%s/analysis/%s/%s";
  
  @Override
  public String getReportUrl(final BenchReport report) {
    return String.format(
        REPORT_URL, 
        report.getProjectId(),
        report.getBenchResultId(),
        report.getId());
  }
}
