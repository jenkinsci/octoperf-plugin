package org.jenkinsci.plugins.octoperf.client;

import org.apache.commons.lang3.tuple.Pair;

import retrofit.RestAdapter;

public interface RestClientService {

  RestClientService CLIENTS = new RetrofitClientService();
  
  Pair<RestAdapter, RestClientAuthenticator> create(String apiUrl);
}
