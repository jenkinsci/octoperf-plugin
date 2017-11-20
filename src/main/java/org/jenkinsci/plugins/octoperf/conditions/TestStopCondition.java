package org.jenkinsci.plugins.octoperf.conditions;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.AbstractBuild;
import hudson.model.Describable;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import org.jenkinsci.plugins.octoperf.result.BenchResult;

import java.io.IOException;
import java.io.PrintStream;

public abstract class TestStopCondition implements Describable<TestStopCondition>, ExtensionPoint {

  @Override
  public StopConditionDescriptor getDescriptor() {
    return (StopConditionDescriptor) Jenkins.getInstance().getDescriptorOrDie(getClass());
  }

  public static ExtensionList<TestStopCondition> all() {
    return Jenkins.getInstance().getExtensionList(TestStopCondition.class);
  }

  public abstract void execute(
    PrintStream logger,
    AbstractBuild<?, ?> build,
    RestApiFactory factory,
    BenchResult result) throws IOException;
}
