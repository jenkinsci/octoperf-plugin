package org.jenkinsci.plugins.octoperf.credentials;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import hudson.model.Item;
import org.jenkinsci.plugins.octoperf.OctoperfCredential;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static hudson.security.ACL.SYSTEM;
import static java.util.Optional.empty;
import static java.util.Optional.of;

final class JenkinsCredentialsService implements CredentialsService {
  private static final List<DomainRequirement> NO_REQUIREMENTS = ImmutableList.of();
  private static final String DEFAULT_SCOPE = "Global";

  @Override
  public Optional<OctoperfCredential> findFirst() {
    for(final OctoperfCredential c : getCredentials(DEFAULT_SCOPE, empty())) {
      return of(c);
    }
    return empty();
  }
  
  @Override
  public Optional<OctoperfCredential> find(final String id) {
    for(final OctoperfCredential c : getCredentials(DEFAULT_SCOPE, empty())) {
      if(Objects.equal(c.getId(),id)) {
        return of(c);
      }
    }
    return empty();
  }

  @Override
  public List<OctoperfCredential> getCredentials(final Object scope, final java.util.Optional<Item> item) {
    final List<OctoperfCredential> result = new ArrayList<OctoperfCredential>();
    final Set<String> ids = new HashSet<String>();

    for (final OctoperfCredential c : CredentialsProvider
        .lookupCredentials(OctoperfCredential.class, item.orElse(null), SYSTEM, NO_REQUIREMENTS)) {
      final String id = c.getId();
      if (!ids.contains(id)) {
        result.add(c);
        ids.add(id);
      }
    }
    return result;
  }
}
