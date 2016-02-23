package org.jenkinsci.plugins.octoperf.result;

import static org.junit.Assert.assertNotNull;

import org.jenkinsci.plugins.octoperf.result.BenchResultState;
import org.junit.Test;

public class BenchResultStateTest {

  @Test
  public void shouldEnumerate() {
    for(final BenchResultState state : BenchResultState.values()) {
      assertNotNull(state.isTerminalState());
      assertNotNull(state.isError());
      assertNotNull(state.isRunning());
    }
  }
}
