package org.jenkinsci.plugins.octoperf.client;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.google.common.testing.NullPointerTester;

import retrofit.RestAdapter;

public class RestClientServiceTest {

  private final RestClientService service = new RetrofitClientService();
  
  @Test
  public void shouldCreateClient() {
    final Pair<RestAdapter, RestClientAuthenticator> pair = service.create("http://localhost");
    assertNotNull(pair.getLeft());
    assertNotNull(pair.getRight());
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(service.getClass(), PACKAGE);
  }
}
