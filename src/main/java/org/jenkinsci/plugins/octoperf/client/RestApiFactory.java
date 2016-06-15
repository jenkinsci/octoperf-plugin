package org.jenkinsci.plugins.octoperf.client;

/**
 *
 * Rest endpoints factory
 *
 * @author gerald
 */
public interface RestApiFactory {

  <T> T create(final Class<T> service);
}
