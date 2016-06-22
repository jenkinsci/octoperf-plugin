package org.jenkinsci.plugins.octoperf.client;


import com.google.common.base.Optional;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jenkinsci.plugins.octoperf.account.AccountApi;
import org.jenkinsci.plugins.octoperf.account.Credentials;
import retrofit2.Call;

import java.io.IOException;
import java.io.PrintStream;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Injects this component by its interface {@link RestClientAuthenticator} into OkHttpClient used by retrofit
 * 
 * @author jerome
 * @author gerald
 *
 */
final class UsernamePasswordRestClientAuthentication implements RestClientAuthenticator {

  private volatile Optional<String> username = Optional.absent();
  private volatile Optional<String> password = Optional.absent();
  private volatile Optional<String> token = Optional.absent();

  private final AccountApi accountApi;
  private final PrintStream logger;

  public UsernamePasswordRestClientAuthentication(final AccountApi accountApi, final PrintStream logger){
    this.accountApi = checkNotNull(accountApi);
    this.logger = checkNotNull(logger);
  }

  @Override
  public Request authenticate(Route route, Response response) throws IOException {
    if (!username.isPresent()){
      return null;
    }

    Optional<Credentials> credentials = Optional.absent();
    try{
      final Call<Credentials> login = accountApi.login(username.get(), password.get());
      credentials = Optional.fromNullable(login.execute().body());
    } catch (final IOException e){
      logger.println("Authentication failed. "+e);
      e.printStackTrace(logger);
      return null;
    }

    if(credentials.isPresent()) {
      token = Optional.of(credentials.get().getId());

      return response.request().newBuilder()
        .header(AUTHENTICATION_HEADER, token.get())
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
    if(token.isPresent()) {
      request = request.newBuilder().addHeader(AUTHENTICATION_HEADER, token.or("")).build();
    }
    return chain.proceed(request);
  }
}
