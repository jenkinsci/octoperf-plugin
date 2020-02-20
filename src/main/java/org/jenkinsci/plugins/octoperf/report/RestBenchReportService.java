package org.jenkinsci.plugins.octoperf.report;

import static org.apache.commons.lang3.StringUtils.removeEnd;

final class RestBenchReportService implements BenchReportService {
  private static final String REPORT_URL = "/#/app/workspace/%s/project/%s/analysis/%s";

  @Override
  public String getReportUrl(
    final String serverUrl,
    final String workspaceId,
    final BenchReport report) {
    final String baseUrl = removeEnd(serverUrl, "/") + "/app";
    return String.format(
      baseUrl + REPORT_URL,
      workspaceId,
      report.getProjectId(),
      report.getId()
    );
  }
}
