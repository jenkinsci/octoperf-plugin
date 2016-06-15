package org.jenkinsci.plugins.octoperf.client;

import com.google.common.annotations.VisibleForTesting;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import org.jenkinsci.plugins.octoperf.account.Credentials;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit2.Call;
import retrofit2.mock.Calls;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsernamePasswordRestClientAuthenticationTest {

  public static final String USERNAME = "username";
  @VisibleForTesting
  static final String NONE = "none";
  public static final String PASSWORD = "password";

  private UsernamePasswordRestClientAuthentication authenticator;

  @Mock
  private AccountApi accountApi;

  private Response response;
  private Request request;

  @Before
  public void before() {
    authenticator = new UsernamePasswordRestClientAuthentication(accountApi, System.out);
    request = new Request.Builder().url("https://octoperf.com").build();
    response = new Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(200).build();
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
    final Call<Credentials> call = Calls.failure(new IOException());
    when(accountApi.login(USERNAME, PASSWORD)).thenReturn(call);
    authenticator.onUsernameAndPassword(USERNAME, PASSWORD);
    assertNull(authenticator.authenticate(null, response));
    verify(accountApi).login(anyString(), anyString());
  }

  @Test
  public void shouldAuthenticate() throws IOException {
    final Call<Credentials> call = Calls.response(new Credentials("id", "userId"));
    when(accountApi.login(USERNAME, PASSWORD)).thenReturn(call);
    authenticator.onUsernameAndPassword(USERNAME, PASSWORD);
    final Request request = authenticator.authenticate(null, response);
    assertNotNull(request);
    assertEquals("id", request.header(RestClientAuthenticator.AUTHENTICATION_HEADER));
  }
}
