package org.jenkinsci.plugins.octoperf.credentials;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import hudson.model.Item;
import org.jenkinsci.plugins.octoperf.OctoperfCredential;

import javax.annotation.Nullable;
import java.util.*;

import static hudson.security.ACL.SYSTEM;
import static java.util.Optional.empty;
import static java.util.Optional.of;

final class JenkinsCredentialsService implements CredentialsService {
  private static final List<DomainRequirement> NO_REQUIREMENTS = ImmutableList.of();
  private static final String DEFAULT_SCOPE = "Global";

  @Override
  public Optional<OctoperfCredential> findFirst(@Nullable final Item item) {
    for (final OctoperfCredential c : getCredentials(DEFAULT_SCOPE, item)) {
      return of(c);
    }
    return empty();
  }
  
  @Override
  public Optional<OctoperfCredential> find(final String id, @Nullable final Item item) {
    for (final OctoperfCredential c : getCredentials(DEFAULT_SCOPE, item)) {
      if (Objects.equal(c.getId(),id)) {
        return of(c);
      }
    }
    return empty();
  }

  @Override
  public List<OctoperfCredential> getCredentials(final Object scope, @Nullable final Item item) {
    final List<OctoperfCredential> result = new ArrayList<>();
    final Set<String> ids = new HashSet<>();

    for (final OctoperfCredential c : CredentialsProvider
      .lookupCredentials(OctoperfCredential.class, item, SYSTEM, NO_REQUIREMENTS)) {
      final String id = c.getId();
      if (!ids.contains(id)) {
        result.add(c);
        ids.add(id);
      }
    }
    return result;
  }
}
