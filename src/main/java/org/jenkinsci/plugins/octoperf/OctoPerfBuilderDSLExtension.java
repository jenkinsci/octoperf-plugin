package org.jenkinsci.plugins.octoperf;

import hudson.Extension;
import javaposse.jobdsl.dsl.helpers.step.StepContext;
import javaposse.jobdsl.plugin.ContextExtensionPoint;
import javaposse.jobdsl.plugin.DslExtensionMethod;
import org.eclipse.jetty.util.log.StdErrLog;

import java.util.Optional;

import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;


@Extension(optional = true)
public class OctoPerfBuilderDSLExtension extends ContextExtensionPoint {
  private static final StdErrLog LOGGER = new StdErrLog("octoperf-jenkins");

  @DslExtensionMethod(context = StepContext.class)
  public Object octoPerfTest(final Runnable closure) {
    LOGGER.info("Running 'octoPerfTest' method from JOB DSL plugin...");

    final OctoPerfBuilderDSLContext context = new OctoPerfBuilderDSLContext();
    executeInContext(closure, context);

    final OctoperfBuilderDescriptor desc = OctoperfBuilderDescriptor.getDescriptor();
    final String serverUrl = desc.getOctoperfURL();

    OctoperfBuilder builder = null;
    try {
      final Optional<OctoperfCredential> credentials = CREDENTIALS_SERVICE.find(context.credentialsId);
      LOGGER.info(context.credentialsId + " is " + (credentials.isPresent() ? "" : "not") + " present in credentials");

      if (credentials.isPresent()) {
        builder = new OctoperfBuilder(
            context.credentialsId,
            context.scenarioId,
            context.getStopConditions()
        );
        builder.setServerUrl(serverUrl);
      }

    } catch (final Exception e) {
      LOGGER.warn("Failed to create OctoPerfBuilder object from Job DSL description: credentialsId=" + context.credentialsId +
          ", scenarioId =" + context.scenarioId + ", serverUrl=" + serverUrl);
    } finally {
      return builder;
    }
  }
}
