package org.jenkinsci.plugins.octoperf.client;

import static org.jenkinsci.plugins.octoperf.client.SecurityRequestInterceptor.AUTHENTICATION_HEADER;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.annotations.VisibleForTesting;

import retrofit.RequestInterceptor.RequestFacade;

@RunWith(MockitoJUnitRunner.class)
public class SecurityRequestInterceptorTest {

  @VisibleForTesting
  static final String NONE = "none";
  
  private SecurityRequestInterceptor interceptor;

  @Mock
  private RequestFacade request;

  @Before
  public void before() {
    interceptor = new SecurityRequestInterceptor();
  }

  @Test
  public void shouldAddApiKeyHeader() {
    interceptor.onApiKey(NONE);
    interceptor.intercept(request);
    verify(request).addHeader(AUTHENTICATION_HEADER, NONE);
  }

  @Test
  public void shouldAddHeaderAfterLogin() {
    interceptor.onApiKey("ApiKey");
    interceptor.intercept(request);
    verify(request).addHeader(AUTHENTICATION_HEADER, "ApiKey");
  }

  @Test
  public void shouldAddHeaderAfterLoginAndLogout() {
    interceptor.onApiKey("ApiKey");
    interceptor.onLogout();
    interceptor.intercept(request);
    verify(request, never()).addHeader(AUTHENTICATION_HEADER, NONE);
  }
}
