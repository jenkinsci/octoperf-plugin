package org.jenkinsci.plugins.octoperf.account;

import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.client.RestClientAuthenticator;

import retrofit.RestAdapter;

public interface AccountService {
  /**
   * 
   */
  AccountService ACCOUNTS = new RetrofitAccountService();
  
  /**
   * Login user on octoperf
   * 
   * @param pair rest adapter + authenticator
   * @param username user name
   * @param password password
   * @return client with login configured
   */
  RestAdapter login(Pair<RestAdapter, RestClientAuthenticator> pair, String username, String password);
}