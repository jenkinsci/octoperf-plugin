package org.jenkinsci.plugins.octoperf.report;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.jenkinsci.plugins.octoperf.constants.Constants.DEFAULT_API_URL;

final class RestBenchReportService implements BenchReportService {

  private static final String DEFAULT_API_URL = "https://api.octoperf.com";
  private static final String SAAS_APP = "https://app.octoperf.com";
  private static final String REPORT_URL = "/#/app/workspace/%s/project/%s/analysis/%s";

  @Override
  public String getReportUrl(
    final String serverUrl,
    final String workspaceId,
    final BenchReport report) {
    final String baseUrl = Objects.equals(serverUrl, DEFAULT_API_URL) ? SAAS_APP : (removeEnd(serverUrl, "/") + "/app");
    return String.format(
      baseUrl + REPORT_URL,
      workspaceId,
      report.getProjectId(),
      report.getId()
    );
  }
}
