package org.jenkinsci.plugins.octoperf.client;

import okhttp3.OkHttpClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.PrintStream;

final class RetrofitClientService implements RestClientService {

  @Override
  public Pair<RestApiFactory, RestClientAuthenticator> create(final String apiUrl, final PrintStream logger) {

    final Retrofit unauthenticatedClient = new Retrofit
        .Builder()
        .addConverterFactory(JacksonConverterFactory.create())
        .client(new OkHttpClient.Builder().build())
        .baseUrl(apiUrl)
        .build();

    final RestClientAuthenticator authenticator = new UsernamePasswordRestClientAuthentication(unauthenticatedClient.create(AccountApi.class), logger);

    final Retrofit client = new Retrofit
        .Builder()
        .addConverterFactory(JacksonConverterFactory.create())
        .client(new OkHttpClient.Builder().authenticator(authenticator).addNetworkInterceptor(authenticator).build())
        .baseUrl(apiUrl)
        .build();

    final RestApiFactory wrapper = new RetrofitWrapper(client);

    return ImmutablePair.of(wrapper, authenticator);
  }

}
