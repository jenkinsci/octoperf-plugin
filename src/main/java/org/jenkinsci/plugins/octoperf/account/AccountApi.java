package org.jenkinsci.plugins.octoperf.account;

import java.util.Optional;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * This REST Api allows to register, login and logout a user on the REST server.
 * 
 * @author jerome
 *
 */
public interface AccountApi {

  /**
   * Logs in the user and sends back {@link CredentialsDTO} if login is successful.
   * 
   * @param username user username
   * @param password user password
   * @return credentials if success, else {@link Optional#absent()}
   */
  @POST("/user/unsecure/login")
  @FormUrlEncoded
  Credentials login(@Field("username") String username, @Field("password") String password);
}
