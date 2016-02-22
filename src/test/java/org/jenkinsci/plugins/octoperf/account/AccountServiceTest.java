package org.jenkinsci.plugins.octoperf.account;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.testing.NullPointerTester;

import retrofit.RestAdapter;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  
  @Mock
  RestAdapter adapter;
  @Mock
  RestClientAuthenticator authenticator;
  @Mock
  AccountApi accounts;
  
  private final AccountService service = AccountService.ACCOUNTS;
  
  @Before
  public void before() {
    when(adapter.create(AccountApi.class)).thenReturn(accounts);
  }
  
  @Test
  public void shouldPassNPETester() {
    new NullPointerTester().testConstructors(RetrofitAccountService.class, PACKAGE);
  }
  
  @Test
  public void shouldLogin() {
    final Credentials creds = new Credentials("apiKey");
    when(accounts.login(USERNAME, PASSWORD)).thenReturn(creds);
    service.login(ImmutablePair.of(adapter, authenticator), USERNAME, PASSWORD);
    verify(adapter).create(AccountApi.class);
    verify(accounts).login(USERNAME, PASSWORD);
    verify(authenticator).onApiKey(creds.getToken());
  }
}
