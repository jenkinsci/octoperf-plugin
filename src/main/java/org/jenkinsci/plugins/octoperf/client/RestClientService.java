package org.jenkinsci.plugins.octoperf.client;

import org.apache.commons.lang3.tuple.Pair;

import retrofit.RestAdapter;

/**
 * Creates a {@link RestAdapter} coupled with a {@link RestClientAuthenticator} 
 * to login on Octoperf.
 * 
 * @author jerome
 *
 */
public interface RestClientService {

  /**
   * Singleton {@link RestClientService} instance.
   */
  RestClientService CLIENTS = new RetrofitClientService();
  
  /**
   * Creates a {@link RestAdapter} ready to login on octoperf cloud load testing platform.
   * @param apiUrl Example: https://api.octoperf.com
   * @return pair of adapter and authenticator
   */
  Pair<RestAdapter, RestClientAuthenticator> create(String apiUrl);
}
