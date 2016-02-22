package org.jenkinsci.plugins.octoperf.runtime;

import static org.junit.Assert.assertNotNull;

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
