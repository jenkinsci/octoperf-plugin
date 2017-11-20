package org.jenkinsci.plugins.octoperf.log;

import com.google.common.collect.ImmutableSet;
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
import retrofit2.mock.Calls;

import java.io.IOException;
import java.io.PrintStream;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.log.LogService.LOGS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JMeterLogServiceTest {
  
  private static final String BENCH_RESULT_ID = "benchResultId";
  
  @Mock
  RestApiFactory apiFactory;
  @Mock
  LogApi api;
  
  @Rule
  public final JenkinsRule jenkins = new JenkinsRule();

  @Before
  public void before() {
    when(apiFactory.create(LogApi.class)).thenReturn(api);
    apiGetFile("results");
    apiGetFile("results2");
    apiGetFile("another-header");
  }

  private void apiGetFile(final String filename) {
    final BufferedSource bufferedSource = mock(BufferedSource.class);
    when(bufferedSource.inputStream()).thenReturn(getClass().getResourceAsStream("/"+filename+".jtl"));
    ResponseBody responseBody = new RealResponseBody(new Headers.Builder().add("Content-Type", "application/octet-stream").build(), bufferedSource);
    when(api.getFile(BENCH_RESULT_ID, filename + ".jtl")).thenReturn(Calls.response(responseBody));
  }

  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(JMeterLogService.class, PACKAGE);
  }
  
  @Test
  public void shouldDownloadLogsAndMergeJTLs() throws IOException, InterruptedException {
    when(api.getFiles(BENCH_RESULT_ID)).thenReturn(Calls.response(ImmutableSet.of("results.jtl", "results2.jtl")));
    final FreeStyleProject project = jenkins.createFreeStyleProject();
    final FilePath workspace = jenkins.jenkins.getWorkspaceFor(project);
    final PrintStream logger = new PrintStream(System.out);
    LOGS.downloadLogFiles(workspace, logger, apiFactory, BENCH_RESULT_ID);
    LOGS.mergeJTLs(workspace, logger);
  }

  @Test
  public void shouldDownloadLogsAndNotMergeJTLs() throws IOException, InterruptedException {
    when(api.getFiles(BENCH_RESULT_ID)).thenReturn(Calls.response(ImmutableSet.of("results.jtl", "another-header.jtl")));
    final FreeStyleProject project = jenkins.createFreeStyleProject();
    final FilePath workspace = jenkins.jenkins.getWorkspaceFor(project);
    final PrintStream logger = new PrintStream(System.out);
    LOGS.downloadLogFiles(workspace, logger, apiFactory, BENCH_RESULT_ID);
    LOGS.mergeJTLs(workspace, logger);
  }
}
