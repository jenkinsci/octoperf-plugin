package org.jenkinsci.plugins.octoperf.credentials;

import java.util.List;

import org.jenkinsci.plugins.octoperf.OctoperfCredential;

import com.google.common.base.Optional;

import hudson.model.Item;

public interface CredentialsService {
  CredentialsService CREDENTIALS_SERVICE = new JenkinsCredentialsService();
  
  Optional<OctoperfCredential> findFirst();
  
  Optional<OctoperfCredential> find(String id);
  
  List<OctoperfCredential> getCredentials(Object scope, Optional<Item> item);
}
