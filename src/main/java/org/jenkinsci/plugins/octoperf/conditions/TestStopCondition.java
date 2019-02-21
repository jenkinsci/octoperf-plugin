package org.jenkinsci.plugins.octoperf.conditions;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Result;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

public abstract class TestStopCondition extends AbstractDescribableImpl<TestStopCondition> implements ExtensionPoint, Serializable {

  public TestStopCondition() {
    super();
  }

  public static ExtensionList<TestStopCondition> all() {
    return Jenkins.get().getExtensionList(TestStopCondition.class);
  }

  public abstract Result execute(
    PrintStream logger,
    RestApiFactory factory,
    String benchResultId) throws IOException;
}
