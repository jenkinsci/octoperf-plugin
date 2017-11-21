package org.jenkinsci.plugins.octoperf.conditions;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Result;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

public abstract class TestStopCondition implements Describable<TestStopCondition>, ExtensionPoint, Serializable {

  public TestStopCondition() {
    super();
  }

  @Override
  public StopConditionDescriptor getDescriptor() {
    return (StopConditionDescriptor) Jenkins.getInstance().getDescriptorOrDie(getClass());
  }

  public static ExtensionList<TestStopCondition> all() {
    return Jenkins.getInstance().getExtensionList(TestStopCondition.class);
  }

  public abstract Result execute(
    PrintStream logger,
    RestApiFactory factory,
    String benchResultId) throws IOException;
}
