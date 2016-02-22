package org.jenkinsci.plugins.octoperf.client;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

final class RetrofitClientService implements RestClientService {

  @Override
  public Pair<RestAdapter, RestClientAuthenticator> create(final String apiUrl) {
    final RestClientAuthenticator interceptor = new SecurityRequestInterceptor();
    
    final RestAdapter client = new RestAdapter
    .Builder()
    .setConverter(new JacksonConverter())
    .setRequestInterceptor(interceptor)
    .setEndpoint(apiUrl)
    .build();
    
    return ImmutablePair.of(client, interceptor);
  }

}
