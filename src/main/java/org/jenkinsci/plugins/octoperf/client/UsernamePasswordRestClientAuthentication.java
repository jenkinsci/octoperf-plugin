package org.jenkinsci.plugins.octoperf.client;


import com.google.common.base.Optional;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import org.jenkinsci.plugins.octoperf.account.SecurityToken;
import org.jenkinsci.plugins.octoperf.date.DateService;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import retrofit2.Call;

import java.io.IOException;
import java.io.PrintStream;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;

/**
 * Injects this component by its interface {@link RestClientAuthenticator} into OkHttpClient used by retrofit
 * 
 * @author jerome
 * @author gerald
 *
 */
final class UsernamePasswordRestClientAuthentication implements RestClientAuthenticator {

  private static final String BEARER = "Bearer ";
  private static final Duration FIVE_MINUTES = Duration.standardMinutes(5);
  private volatile Optional<String> username = Optional.absent();
  private volatile Optional<String> password = Optional.absent();
  private volatile Optional<SecurityToken> token = Optional.absent();

  private final AccountApi accountApi;
  private final PrintStream logger;

  UsernamePasswordRestClientAuthentication(final AccountApi accountApi, final PrintStream logger){
    this.accountApi = checkNotNull(accountApi);
    this.logger = checkNotNull(logger);
  }

  @Override
  public Request authenticate(final Route route, final Response response) throws IOException {
    if (!username.isPresent()) {
      return null;
    }

    Optional<SecurityToken> optional;
    try {
      final Call<SecurityToken> login = accountApi.login(username.get(), password.get());
      optional = Optional.fromNullable(login.execute().body());
    } catch (final IOException e){
      logger.println("Authentication failed. "+e);
      e.printStackTrace(logger);
      return null;
    }

    if(optional.isPresent()) {
      token = optional;

      return response.request().newBuilder()
        .header(AUTHORIZATION, BEARER + token.get().getToken())
        .build();
    }
    return null;
  }

  @Override
  public void onUsernameAndPassword(final String username, final String password) {
    this.username = fromNullable(username);
    this.password = fromNullable(password);
  }
  
  @Override
  public void onLogout() {
    this.username = Optional.absent();
    this.password = Optional.absent();

  }

  @Override
  public Response intercept(final Chain chain) throws IOException {
    Request request = chain.request();
    if (token.isPresent()) {
      token = Optional.of(refreshToken(token.get()));
      final String tokenStr = token.get().getToken();
      request = request.newBuilder().addHeader(AUTHORIZATION, BEARER + tokenStr).build();
    }
    return chain.proceed(request);
  }

  private SecurityToken refreshToken(final SecurityToken securityToken) throws IOException {
    final DateTime now = DateService.DATES.now();
    final Duration duration = new Duration(now, securityToken.getExpiresAt());
    if (duration.isShorterThan(FIVE_MINUTES)) {
      final Call<SecurityToken> callable = accountApi.refreshToken();
      return callable.execute().body();
    }
    return securityToken;
  }
}
