package org.jenkinsci.plugins.octoperf.account;


import com.google.common.base.Optional;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * This REST Api allows to register, login and logout a user on the REST server.
 * 
 * @author jerome
 *
 */
public interface AccountApi {

  /**
   * Logs in the user and sends back {@link SecurityToken} if login is successful.
   * 
   * @param username user username
   * @param password user password
   * @return credentials if success, else {@link Optional#absent()}
   */
  @POST("/public/users/login")
  @FormUrlEncoded
  Call<SecurityToken> login(@Field("username") String username, @Field("password") String password);
}
