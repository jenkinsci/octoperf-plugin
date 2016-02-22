package org.jenkinsci.plugins.octoperf.client;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import com.google.common.annotations.VisibleForTesting;

import retrofit.RequestInterceptor;

/**
 * Injects this component by its interface {@link RestClientAuthenticator} into the Vaadin component
 * which handles login/logout.
 * 
 * @author jerome
 *
 */
final class SecurityRequestInterceptor implements RequestInterceptor, RestClientAuthenticator {
  @VisibleForTesting
  static final String NONE = "none";
  static final String AUTHENTICATION_HEADER = "AuthenticationToken";

  private volatile Optional<String> apiKey = Optional.empty();

  @Override
  public void intercept(final RequestFacade request) {
    if(apiKey.isPresent()) {
      request.addHeader(AUTHENTICATION_HEADER, apiKey.get());
    }
  }

  @Override
  public void onApiKey(final String apiKey) {
    this.apiKey = ofNullable(apiKey);
  }
  
  @Override
  public void onLogout() {
    this.apiKey = empty();
  }
}
