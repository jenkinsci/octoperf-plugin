package org.jenkinsci.plugins.octoperf.client;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.google.common.testing.NullPointerTester;
import retrofit2.Retrofit;


public class RestClientServiceTest {

  private final RestClientService service = new RetrofitClientService();
  
  @Test
  public void shouldCreateClient() {
    final Pair<RestApiFactory, RestClientAuthenticator> pair = service.create("http://localhost", System.out);
    assertNotNull(pair.getLeft());
    assertNotNull(pair.getRight());
  }

  @Test
  public void shouldCreateSingleton(){
    assertNotNull(RestClientService.CLIENTS);
  }

  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(service.getClass(), PACKAGE);
  }
}
