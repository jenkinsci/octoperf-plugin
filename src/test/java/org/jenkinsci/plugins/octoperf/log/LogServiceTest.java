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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.log.LogService.LOGS;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {
  
  private static final String BENCH_RESULT_ID = "benchResultId";
  
  @Mock
  RestApiFactory apiFactory;
  @Mock
  LogApi api;
  
  @Rule
  public final JenkinsRule jenkins = new JenkinsRule();

  @Mock
  BufferedSource bufferedSource;

  @Before
  public void before() {
    when(apiFactory.create(LogApi.class)).thenReturn(api);
    when(api.getFiles(BENCH_RESULT_ID)).thenReturn(Calls.response((Set<String>)ImmutableSet.of("filename.json")));
    when(bufferedSource.inputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
    ResponseBody responseBody = new RealResponseBody(new Headers.Builder().add("Content-Type", "application/octet-stream").build(), bufferedSource);
    when(api.getFile(BENCH_RESULT_ID, "filename.json")).thenReturn(Calls.response(responseBody));
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(JMeterLogService.class, PACKAGE);
  }
  
  @Test
  public void shouldCreateJUnitReport() throws IOException, InterruptedException {
    final FreeStyleProject project = jenkins.createFreeStyleProject();
    final FilePath workspace = jenkins.jenkins.getWorkspaceFor(project);
    LOGS.downloadLogFiles(workspace, new PrintStream(System.out), apiFactory, BENCH_RESULT_ID);
  }
}
