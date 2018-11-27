package org.jenkinsci.plugins.octoperf.junit;

import com.google.common.testing.NullPointerTester;
import hudson.FilePath;
import hudson.model.FreeStyleProject;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okio.BufferedSource;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit2.Call;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.junit.JUnitReportService.JUNIT_REPORTS;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JUnitReportServiceTest {
  
  private static final String BENCH_RESULT_ID = "benchResultId";
  
  @Mock
  RestApiFactory retrofit;
  @Mock
  JUnitReportApi api;
  @Mock
  Call<ResponseBody> call;
  @Mock
  BufferedSource bufferedSource;

  @Rule
  public final JenkinsRule jenkins = new JenkinsRule();
  
  @Before
  public void before() throws IOException {
    when(retrofit.create(JUnitReportApi.class)).thenReturn(api);
    when(bufferedSource.inputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
    final ResponseBody responseBody = new RealResponseBody("application/octet-stream", 0, bufferedSource);
    when(call.execute()).thenReturn(Response.success(responseBody));
    when(api.getReport(BENCH_RESULT_ID)).thenReturn(call);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RestJUnitReportService.class, PACKAGE);
  }
  
  @Test
  public void shouldCreateJUnitReport() throws IOException, InterruptedException {
    final FreeStyleProject project = jenkins.createFreeStyleProject();
    final FilePath workspace = jenkins.jenkins.getWorkspaceFor(project);
    JUNIT_REPORTS.saveJUnitReport(workspace, retrofit, BENCH_RESULT_ID);
  }
}
