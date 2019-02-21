package org.jenkinsci.plugins.octoperf.conditions;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

public abstract class StopConditionDescriptor extends Descriptor<TestStopCondition> {

  public final String getId() {
    return getClass().getName();
  }

  public static DescriptorExtensionList<TestStopCondition, StopConditionDescriptor> all() {
    return Jenkins.getInstanceOrNull().getDescriptorList(TestStopCondition.class);
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
