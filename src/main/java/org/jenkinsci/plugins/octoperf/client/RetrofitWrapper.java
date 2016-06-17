package org.jenkinsci.plugins.octoperf.client;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import retrofit2.Retrofit;

import static lombok.AccessLevel.PACKAGE;

/**
 * Retrofit wrapper. As retrofit is a final class this is mandatory to unit test our code ...
 */
@AllArgsConstructor(access = PACKAGE)
final class RetrofitWrapper implements RestApiFactory {

  @NonNull
  private final Retrofit retrofit;

  public <T> T create(final Class<T> service){
    return retrofit.create(service);
  }
}
