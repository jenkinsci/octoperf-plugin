package org.jenkinsci.plugins.octoperf;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.jenkinsci.plugins.octoperf.constants.Constants.DEFAULT_API_URL;
import static org.junit.Assert.assertEquals;

public class GlobalUISettingsTest {

  @Rule
  public JenkinsRule jenkins = new JenkinsRule();

  public HtmlPage settings;
  public JenkinsRule.WebClient webClient;

  @Before
  public void setUp() throws SAXException,IOException {
    webClient = jenkins.createWebClient();
    settings = webClient.goTo("configure");
  }

  @Test
  public void shouldTestOctoperfUrl() throws SAXException,IOException,Exception {
    final HtmlElement octperfURL = settings.getElementByName("_.octoperfURL");
    octperfURL.setValue(DEFAULT_API_URL);
    final HtmlForm submit = settings.getFormByName("config");
    jenkins.submit(submit);
    final String apiUrl = settings.getElementByName("_.octoperfURL").getAttribute("value");
    assertEquals(DEFAULT_API_URL, apiUrl);
  }
}