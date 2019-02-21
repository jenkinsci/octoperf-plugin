package org.jenkinsci.plugins.octoperf.conditions;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.*;

public class StopConditionDescriptorTest {

  @Rule
  public JenkinsRule j = new JenkinsRule();

  @After
  public void shutdown() throws Exception {
    j.after();
  }

  @Test
  public void shouldRegisterDescriptor() throws Exception {
    final StopConditionDescriptor descriptor = StopConditionDescriptor.getById(StopOnAlert.DescriptorImpl.class.getName());
    assertNotNull(descriptor);
    assertTrue(descriptor instanceof StopOnAlert.DescriptorImpl);

    assertNull(StopConditionDescriptor.getById("null"));
  }
}