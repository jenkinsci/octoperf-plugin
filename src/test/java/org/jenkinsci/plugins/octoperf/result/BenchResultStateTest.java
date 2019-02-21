package org.jenkinsci.plugins.octoperf.result;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
