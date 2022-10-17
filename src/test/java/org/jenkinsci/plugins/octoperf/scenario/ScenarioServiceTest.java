package org.jenkinsci.plugins.octoperf.scenario;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.report.BenchReport;
import org.jenkinsci.plugins.octoperf.report.BenchReportTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.mock.Calls;

import java.io.IOException;
import java.util.List;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static java.util.Optional.empty;
import static org.jenkinsci.plugins.octoperf.scenario.ScenarioService.SCENARIOS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ScenarioServiceTest {
  
  private static final Scenario SCENARIO = ScenarioTest.newInstance();
  private static final String SCENARIO_ID = "scenarioId";
  private static final BenchReport BENCH_REPORT = BenchReportTest.newInstance();
  
  @Mock
  RestApiFactory retrofit;
  @Mock
  ScenarioApi api;

  @Before
  public void before() {
    when(retrofit.create(ScenarioApi.class)).thenReturn(api);
  }
  
  @Test
  public void shouldRun() throws IOException {
    when(api.run(eq(SCENARIO_ID), any())).thenReturn(Calls.response(BENCH_REPORT));
    final BenchReport report = SCENARIOS.startTest(retrofit, SCENARIO_ID, empty());
    assertSame(BENCH_REPORT, report);
    verify(api).run(SCENARIO_ID, null);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RetrofitScenarioService.class, PACKAGE);
  }
  
  @Test
  public void shouldFind() throws IOException{
    when(api.find(SCENARIO.getId())).thenReturn(Calls.response(SCENARIO));
    SCENARIOS.find(retrofit, SCENARIO.getId());
    verify(retrofit).create(ScenarioApi.class);
    verify(api).find(SCENARIO.getId());
  }
  
  @Test
  public void shouldListScenariosPerProject() throws IOException{
    when(api.list(anyString())).thenReturn(Calls.response(ImmutableList.of(SCENARIO)));
    final List<Scenario> table = SCENARIOS.getScenariosByProject(retrofit, "workspaceId");
    assertFalse(table.isEmpty());
    verify(retrofit).create(ScenarioApi.class);
    verify(api).list(anyString());
  }
  
}
