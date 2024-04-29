package org.jenkinsci.plugins.octoperf.conditions;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

import java.util.concurrent.atomic.AtomicReference;

public abstract class StopConditionDescriptor extends Descriptor<TestStopCondition> {
  public static final AtomicReference<Jenkins> JENKINS = new AtomicReference<>(Jenkins.getInstanceOrNull());

  public final String getId() {
    return getClass().getName();
  }

  public static DescriptorExtensionList<TestStopCondition, StopConditionDescriptor> all() {
    return JENKINS.get().getDescriptorList(TestStopCondition.class);
  }

  public static StopConditionDescriptor getById(String id) {
    for (StopConditionDescriptor d : all()) {
      if (d.getId().equals(id)) {
        return d;
      }
    }
    return null;
  }
}
