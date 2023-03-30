package org.jenkinsci.plugins.octoperf.client;


import com.google.common.base.Optional;
import com.google.common.base.Strings;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import org.jenkinsci.plugins.octoperf.account.SecurityToken;
import retrofit2.Call;

import java.io.IOException;
import java.io.PrintStream;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;

/**
 * Injects this component by its interface {@link RestClientAuthenticator} into OkHttpClient used by retrofit
 *
 * @author jerome
 * @author gerald
 *
 */
final class BearerClientAuthentication implements RestClientAuthenticator {

  private static final String BEARER = "Bearer ";
  private volatile Optional<String> username = absent();
  private volatile Optional<String> password = absent();
  private volatile Optional<SecurityToken> token = absent();

  private final AccountApi accountApi;
  private final PrintStream logger;

  BearerClientAuthentication(final AccountApi accountApi, final PrintStream logger){
    this.accountApi = checkNotNull(accountApi);
    this.logger = checkNotNull(logger);
  }

  @Override
  public Request authenticate(final Route route, final Response response) {
    Optional<SecurityToken> token;
    try {
      if (username.isPresent()) {
        final String login = username.get();
        final Call<SecurityToken> call = accountApi.login(login, password.get());
        token = fromNullable(call.execute().body());
      } else {
        token = password.transform(SecurityToken::new);
      }
    } catch (final IOException e){
      logger.println("Authentication failed. "+e);
      e.printStackTrace(logger);
      return null;
    }

    if (token.isPresent()) {
      this.token = token;

      if (response.request().headers(AUTHORIZATION).isEmpty()) {
        return response
          .request()
          .newBuilder()
          .header(AUTHORIZATION, BEARER + token.get().getToken())
          .build();
      }
    }

    return null;
  }

  @Override
  public void onUsernameAndPassword(final String username, final String password) {
    this.username = fromNullable(emptyToNull(username.trim()));
    this.password = fromNullable(emptyToNull(password.trim()));
  }

  @Override
  public void onLogout() {
    this.username = absent();
    this.password = absent();
  }

  @Override
  public Response intercept(final Chain chain) throws IOException {
    Request request = chain.request();
    if (token.isPresent()) {
      final String tokenStr = token.get().getToken();
      request = request.newBuilder().header(AUTHORIZATION, BEARER + tokenStr).build();
    }
    return chain.proceed(request);
  }
}
