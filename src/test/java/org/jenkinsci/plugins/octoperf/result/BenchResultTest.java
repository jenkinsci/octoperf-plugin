package org.jenkinsci.plugins.octoperf.result;

import com.google.common.testing.NullPointerTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.ABORTED;
import static org.jenkinsci.plugins.octoperf.result.BenchResultState.FINISHED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests {@link BenchResult}.
 * 
 * @author jerome
 *
 */
public class BenchResultTest {

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(BenchResult.class).verify();
  }
  
  @Test
  public void shouldPassNullPointerTester() {
    new NullPointerTester().testConstructors(BenchResult.class, PACKAGE);
  }
  
  @Test
  public void shouldWither() {
    final BenchResult result = newInstance();
    final BenchResult withState = result.withState(result.getState());
    assertSame(result, withState);
    assertEquals(ABORTED, result.withState(ABORTED).getState());
  }
  
  @Test
  public void shouldCreate() {
    final BenchResult report = newInstance();
    assertNotNull(report);
  }
  
  public static BenchResult newInstance() {
    return new BenchResult("id", "designProjectId", "resultProjectId", FINISHED);
  }
  
}
