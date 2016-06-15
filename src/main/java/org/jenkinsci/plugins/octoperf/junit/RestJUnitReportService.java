package org.jenkinsci.plugins.octoperf.junit;

import com.google.common.io.Closer;
import hudson.FilePath;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class RestJUnitReportService implements JUnitReportService {

  private static final String JUNIT_REPORT_XML = "junit-report.xml";

  @Override
  public FilePath saveJUnitReport(
      final FilePath workspace, 
      final RestApiFactory apiFactory,
      final String benchResultId) throws IOException, InterruptedException {
    final FilePath path = new FilePath(workspace, JUNIT_REPORT_XML);
    final JUnitReportApi api = apiFactory.create(JUnitReportApi.class);
    final Call<ResponseBody> report = api.getReport(benchResultId);
    final ResponseBody body = report.execute().body();
    final Closer closer = Closer.create();
    try {
      final InputStream input = closer.register(body.byteStream());
      final OutputStream output = closer.register(path.write());
      IOUtils.copy(input, output);
    } finally {
      closer.close();
    }
    return path;
  }

}
