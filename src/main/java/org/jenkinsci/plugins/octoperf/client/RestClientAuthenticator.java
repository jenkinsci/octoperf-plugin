package org.jenkinsci.plugins.octoperf.client;

import okhttp3.Authenticator;
import okhttp3.Interceptor;

public interface RestClientAuthenticator extends Authenticator, Interceptor {

  String AUTHENTICATION_HEADER = "AuthenticationToken";

  /**
   * When passing crednetials.
   * 
   * @param username
   * @param password
   */
  void onUsernameAndPassword(final String username, final String password);
  
  /**
   * Logs out the client.
   */
  void onLogout();
}
