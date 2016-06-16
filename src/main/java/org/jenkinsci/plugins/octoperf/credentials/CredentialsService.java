package org.jenkinsci.plugins.octoperf.credentials;

import java.util.List;

import com.google.common.base.Optional;

import hudson.model.Item;
import org.jenkinsci.plugins.octoperf.OctoperfCredential;

/**
 * Facility for manipulating Jenkins Credentials.
 * 
 * @author jerome
 *
 */
public interface CredentialsService {
  /**
   * Singleton {@link CredentialsService} instance.
   */
  CredentialsService CREDENTIALS_SERVICE = new JenkinsCredentialsService();
  
  /**
   * Finds the first available account.
   * 
   * @return octoperf account, if any
   */
  Optional<OctoperfCredential> findFirst();
  
  /**
   * Finds the account with the given id.
   * 
   * @param id account id
   * @return
   */
  Optional<OctoperfCredential> find(String id);
  
  /**
   * Lists the credentials available in the given scope.
   * 
   * @param scope credentials scope
   * @param item item, if any
   * @return
   */
  List<OctoperfCredential> getCredentials(Object scope, Optional<Item> item);
}
