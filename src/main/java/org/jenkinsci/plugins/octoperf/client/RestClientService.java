package org.jenkinsci.plugins.octoperf.client;

import jenkins.model.Jenkins;
import org.apache.commons.lang3.tuple.Pair;
import retrofit2.Retrofit;

import java.io.PrintStream;

/**
 * Creates a {@link Retrofit} coupled with a {@link RestClientAuthenticator}
 * to login on Octoperf.
 *
 * @author jerome
 */
public interface RestClientService {

  /**
   * Singleton {@link RestClientService} instance.
   */
  RestClientService CLIENTS = new RetrofitClientService(Jenkins.get());

  /**
   * Creates a {@link Retrofit} ready to login on octoperf cloud load testing platform.
   *
   * @param apiUrl Example: https://api.octoperf.com
   * @param logger
   * @return pair of adapter and authenticator
   */
  Pair<RestApiFactory, RestClientAuthenticator> create(String apiUrl, final PrintStream logger);
}
