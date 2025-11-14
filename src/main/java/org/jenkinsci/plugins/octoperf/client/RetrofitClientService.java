package org.jenkinsci.plugins.octoperf.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import hudson.ProxyConfiguration;
import jenkins.model.Jenkins;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import org.jenkinsci.plugins.octoperf.account.LoginFailed;
import org.jenkinsci.plugins.octoperf.account.LoginSuccessful;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
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
    mapper.registerSubtypes(new NamedType(LoginSuccessful.class, "LoginSuccessful"));
    mapper.registerSubtypes(new NamedType(LoginFailed.class, "LoginFailed"));


    final Retrofit unauthenticatedClient = new Retrofit
      .Builder()
      .addConverterFactory(JacksonConverterFactory.create(mapper))
      .client(newClient().build())
      .baseUrl(apiUrl)
      .build();

    final RestClientAuthenticator authenticator = new BearerClientAuthentication(unauthenticatedClient.create(AccountApi.class), logger);

    final OkHttpClient.Builder httpClient = newClient()
      .authenticator(authenticator)
      .addNetworkInterceptor(authenticator);

    final Retrofit retrofit = new Retrofit
      .Builder()
      .addConverterFactory(JacksonConverterFactory.create(mapper))
      .client(httpClient.build())
      .baseUrl(apiUrl)
      .build();

    final RestApiFactory wrapper = new RetrofitWrapper(retrofit);

    return ImmutablePair.of(wrapper, authenticator);
  }

  private OkHttpClient.Builder newClient() {
    try {
      final X509TrustManager trustManager = new TrustingX509TrustManager();
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, new TrustManager[] {trustManager}, new java.security.SecureRandom());

      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      final OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .readTimeout(1, MINUTES)
        .writeTimeout(1, MINUTES)
        .sslSocketFactory(sslSocketFactory, trustManager);

      proxy.ifPresent(cfg -> setProxy(cfg, builder));

      return builder;
    } catch (final GeneralSecurityException e) {
      throw new RuntimeException("Could not initialize SSL Context", e);
    }
  }

  private static void setProxy(
    final ProxyConfiguration cfg,
    final OkHttpClient.Builder builder) {

    builder.proxy(new Proxy(HTTP, new InetSocketAddress(cfg.name, cfg.port)));
    final String username = nullToEmpty(cfg.getUserName());
    final String password = nullToEmpty(cfg.getSecretPassword().getPlainText());

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
