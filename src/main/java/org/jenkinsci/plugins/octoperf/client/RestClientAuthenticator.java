package org.jenkinsci.plugins.octoperf.client;

import retrofit.RequestInterceptor;

/**
 * Manages the Rest client authentication token.
 * 
 * <p>On Vaadin side, this interface must be injected and called 
 * on login and logout.</p>
 * 
 * <p>It allows the REST client to manage access to secured area automatically.</p>
 * 
 * @author jerome
 *
 */
public interface RestClientAuthenticator extends RequestInterceptor {
  /**
   * When passing an api key.
   * 
   * @param apiKey
   */
  void onApiKey(final String apiKey);
  
  /**
   * Logs out the client.
   */
  void onLogout();
}
