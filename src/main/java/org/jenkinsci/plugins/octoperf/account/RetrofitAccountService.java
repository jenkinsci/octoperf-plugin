package org.jenkinsci.plugins.octoperf.account;

import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;

import retrofit.RestAdapter;

final class RetrofitAccountService implements AccountService {

  @Override
  public RestAdapter login(
      final Pair<RestAdapter, RestClientAuthenticator> pair,
      final String username, 
      final String password) {
    
    final RestAdapter adapter = pair.getLeft();
    final RestClientAuthenticator interceptor = pair.getRight();
    
    final AccountApi users = adapter.create(AccountApi.class);
    final Credentials credentials = users.login(username, password);
    interceptor.onApiKey(credentials.getToken());
    return adapter;
  }

}
