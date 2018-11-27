package org.jenkinsci.plugins.octoperf.client;

import hudson.ProxyConfiguration;
import jenkins.model.Jenkins;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RestClientServiceTest {

  @Mock
  Jenkins jenkins;

  private RestClientService service;

  @Before
  public void setUp() {
    service = new RetrofitClientService(jenkins);
  }

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
}
