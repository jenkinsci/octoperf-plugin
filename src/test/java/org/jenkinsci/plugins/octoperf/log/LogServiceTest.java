package org.jenkinsci.plugins.octoperf.log;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.log.LogService.LOGS;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.testing.NullPointerTester;

import hudson.FilePath;
import hudson.model.FreeStyleProject;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {
  
  private static final String BENCH_RESULT_ID = "benchResultId";
  
  @Mock
  RestAdapter adapter;
  @Mock
  LogApi api;
  
  @Rule
  public final JenkinsRule jenkins = new JenkinsRule();
  
  @Before
  public void before() {
    when(adapter.create(LogApi.class)).thenReturn(api);
    when(api.getFiles(BENCH_RESULT_ID)).thenReturn(ImmutableSet.of("filename.json"));
    
    final TypedByteArray body = new TypedByteArray("application/octet-stream", new byte[0]);
    final Response response = new Response("", 200, "OK", ImmutableList.<Header> of(), body);
    when(api.getFile(BENCH_RESULT_ID, "filename.json")).thenReturn(response);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(JMeterLogService.class, PACKAGE);
  }
  
  @Test
  public void shouldCreateJUnitReport() throws IOException, InterruptedException {
    final FreeStyleProject project = jenkins.createFreeStyleProject();
    final FilePath workspace = jenkins.jenkins.getWorkspaceFor(project);
    LOGS.downloadLogFiles(workspace, new PrintStream(System.out), adapter, BENCH_RESULT_ID);
  }
}
