package org.jenkinsci.plugins.octoperf.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;

import static org.junit.Assert.assertNotNull;

public class RetrofitWrapperTest {

  Retrofit retrofit;
  RetrofitWrapper wrapper;

  @Before
  public void before(){
    retrofit = new Retrofit.Builder().baseUrl("http://localhost").build();
    wrapper = new RetrofitWrapper(retrofit);
  }

  @Test
  public void shouldCreateRestApi(){
    assertNotNull(wrapper.create(TestApi.class));
  }
}
