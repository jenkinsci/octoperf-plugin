package org.jenkinsci.plugins.octoperf.client;

import okhttp3.Authenticator;

public interface RestClientAuthenticator extends Authenticator {

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
