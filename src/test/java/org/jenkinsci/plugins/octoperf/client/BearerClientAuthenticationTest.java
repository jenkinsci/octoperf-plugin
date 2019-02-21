package org.jenkinsci.plugins.octoperf.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.net.HttpHeaders;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import org.jenkinsci.plugins.octoperf.account.SecurityToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.Call;
import retrofit2.mock.Calls;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BearerClientAuthenticationTest {

  public static final String USERNAME = "username";
  @VisibleForTesting
  static final String NONE = "none";
  public static final String PASSWORD = "password";

  private BearerClientAuthentication authenticator;

  @Mock
  private AccountApi accountApi;

  @Mock
  private Interceptor.Chain chain;

  private Response response;
  private Request request;

  @Captor
  ArgumentCaptor<Request> captor;

  @Before
  public void before() {
    authenticator = new BearerClientAuthentication(accountApi, System.out);
    request = new Request.Builder().url("https://octoperf.com").build();
    response = new Response.Builder()
      .request(request)
      .message("")
      .protocol(Protocol.HTTP_1_1)
      .code(200)
      .build();
  }

  @Test
  public void shouldNotAuthenticateWithoutUsername() throws IOException {
    authenticator.onUsernameAndPassword(USERNAME, PASSWORD);
    authenticator.onLogout();
    assertNull(authenticator.authenticate(null, response));
    verify(accountApi, times(0)).login(anyString(), anyString());
  }

  @Test
  public void shouldNotAuthenticateWithoutCredentials() throws IOException {
    final Call<SecurityToken> call = Calls.failure(new IOException());
    when(accountApi.login(USERNAME, PASSWORD)).thenReturn(call);
    authenticator.onUsernameAndPassword(USERNAME, PASSWORD);
    assertNull(authenticator.authenticate(null, response));
    verify(accountApi).login(anyString(), anyString());
  }

  @Test
  public void shouldAuthenticate() throws IOException {
    final Call<SecurityToken> call = Calls.response(new SecurityToken("id"));
    when(accountApi.login(USERNAME, PASSWORD)).thenReturn(call);
    authenticator.onUsernameAndPassword(USERNAME, PASSWORD);
    final Request request = authenticator.authenticate(null, response);
    assertNotNull(request);
    assertEquals("Bearer id", request.header(HttpHeaders.AUTHORIZATION));
    when(chain.request()).thenReturn(request);
    authenticator.intercept(chain);
    verify(chain).proceed(captor.capture());
    assertEquals("Bearer id", captor.getValue().header(HttpHeaders.AUTHORIZATION));
  }
}
