package org.jenkinsci.plugins.octoperf.credentials;

import static hudson.security.ACL.SYSTEM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import hudson.model.Item;
import org.jenkinsci.plugins.octoperf.OctoperfCredential;

final class JenkinsCredentialsService implements CredentialsService {
  private static final List<DomainRequirement> NO_REQUIREMENTS = ImmutableList.of();
  private static final String DEFAULT_SCOPE = "Global";

  @Override
  public Optional<OctoperfCredential> findFirst() {
    for(final OctoperfCredential c : getCredentials(DEFAULT_SCOPE, Optional.<Item> absent())) {
      return Optional.of(c);
    }
    return Optional.absent();
  }
  
  @Override
  public Optional<OctoperfCredential> find(final String id) {
    for(final OctoperfCredential c : getCredentials(DEFAULT_SCOPE, Optional.<Item> absent())) {
      if(Objects.equal(c.getId(),id)) {
        return Optional.of(c);
      }
    }
    return Optional.absent();
  }
  
  @Override
  public List<OctoperfCredential> getCredentials(final Object scope, final Optional<Item> item) {
    final List<OctoperfCredential> result = new ArrayList<OctoperfCredential>();
    final Set<String> ids = new HashSet<String>();

    for (final OctoperfCredential c : CredentialsProvider
        .lookupCredentials(OctoperfCredential.class, item.orNull(), SYSTEM, NO_REQUIREMENTS)) {
      final String id = c.getId();
      if (!ids.contains(id)) {
        result.add(c);
        ids.add(id);
      }
    }
    return result;
  }
}
