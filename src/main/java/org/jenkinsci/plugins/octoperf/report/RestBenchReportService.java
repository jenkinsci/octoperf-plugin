package org.jenkinsci.plugins.octoperf.report;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.removeEnd;

final class RestBenchReportService implements BenchReportService {
  private static final String REPORT_URL = "/ui/workspace/%s/project/%s/analysis/report/%s";

  @Override
  public String getReportUrl(
    final String serverUrl,
    final String workspaceId,
    final String projectId,
    final String reportId) {
    final String baseUrl = removeEnd(serverUrl, "/");
    return format(baseUrl + REPORT_URL, workspaceId, projectId, reportId);
  }
}
