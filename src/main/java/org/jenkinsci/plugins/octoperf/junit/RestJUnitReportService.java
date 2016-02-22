package org.jenkinsci.plugins.octoperf.junit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.io.Closer;

import hudson.FilePath;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

final class RestJUnitReportService implements JUnitReportService {

  private static final String JUNIT_REPORT_XML = "junit-report.xml";

  @Override
  public FilePath saveJUnitReport(
      final FilePath workspace, 
      final RestAdapter adapter, 
      final String benchResultId) throws IOException, InterruptedException {
    final FilePath path = new FilePath(workspace, JUNIT_REPORT_XML);
    final JUnitReportApi api = adapter.create(JUnitReportApi.class);
    final Response report = api.getReport(benchResultId);
    final TypedInput body = report.getBody();
    final Closer closer = Closer.create();
    try {
      final InputStream input = closer.register(body.in());
      final OutputStream output = closer.register(path.write());
      IOUtils.copy(input, output);
    } finally {
      closer.close();
    }
    return path;
  }

}
