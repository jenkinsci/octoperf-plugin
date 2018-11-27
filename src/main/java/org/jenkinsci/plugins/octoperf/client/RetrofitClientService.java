package org.jenkinsci.plugins.octoperf.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.ProxyConfiguration;
import jenkins.model.Jenkins;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;
import static java.net.Proxy.Type.HTTP;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

final class RetrofitClientService implements RestClientService {

  private final Optional<ProxyConfiguration> proxy;

  RetrofitClientService(final Jenkins jenkins) {
    super();
    proxy = ofNullable(jenkins).map(j -> j.proxy);
  }

  @Override
  public Pair<RestApiFactory, RestClientAuthenticator> create(final String apiUrl, final PrintStream logger) {

    final ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();

    final Retrofit unauthenticatedClient = new Retrofit
      .Builder()
      .addConverterFactory(JacksonConverterFactory.create(mapper))
      .client(new OkHttpClient.Builder().build())
      .baseUrl(apiUrl)
      .build();

    final RestClientAuthenticator authenticator = new BearerClientAuthentication(unauthenticatedClient.create(AccountApi.class), logger);

    final OkHttpClient.Builder builder = new OkHttpClient.Builder()
      .readTimeout(1, MINUTES)
      .writeTimeout(1, MINUTES)
      .hostnameVerifier(new NoopHostnameVerifier())
      .authenticator(authenticator)
      .addNetworkInterceptor(authenticator);

    proxy.ifPresent(cfg -> setProxy(cfg, builder));

    final Retrofit client = new Retrofit
      .Builder()
      .addConverterFactory(JacksonConverterFactory.create(mapper))
      .client(builder.build())
      .baseUrl(apiUrl)
      .build();

    final RestApiFactory wrapper = new RetrofitWrapper(client);

    return ImmutablePair.of(wrapper, authenticator);
  }

  private void setProxy(
    final ProxyConfiguration cfg,
    final OkHttpClient.Builder builder) {

    builder
      .proxy(new Proxy(HTTP, new InetSocketAddress(cfg.name, cfg.port)));
    final String username = nullToEmpty(cfg.getUserName());
    final String password = nullToEmpty(cfg.getPassword());

    if (isNotEmpty(username) && isNotEmpty(password)) {
      final String credentials = Credentials.basic(username, password);

      final Authenticator proxyAuthenticator = (route, response) -> response
        .request()
        .newBuilder()
        .header("Proxy-Authorization", credentials)
        .build();

      builder.proxyAuthenticator(proxyAuthenticator);
    }
  }

}
