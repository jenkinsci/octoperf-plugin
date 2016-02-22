package org.jenkinsci.plugins.octoperf.report;

import org.jenkinsci.plugins.octoperf.runtime.BenchResult;

import retrofit.RestAdapter;

final class RestBenchReportService implements BenchReportService {
  
  private static final String REPORT_URL = "https://app.octoperf.com/#/app/project/%s/analysis/%s/%s";
  
  @Override
  public BenchReport createReport(final RestAdapter adapter, final BenchResult result) {
    final BenchReportApi api = adapter.create(BenchReportApi.class);
    return api.createDefault(result.getId());
  }

  @Override
  public String getReportUrl(final BenchResult result, final BenchReport report) {
    return String.format(
        REPORT_URL, 
        result.getDesignProjectId(),
        result.getId(),
        report.getId());
  }
}
