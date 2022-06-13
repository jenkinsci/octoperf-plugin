package org.jenkinsci.plugins.octoperf;

import hudson.Extension;
import javaposse.jobdsl.dsl.helpers.step.StepContext;
import javaposse.jobdsl.plugin.ContextExtensionPoint;
import javaposse.jobdsl.plugin.DslExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static org.jenkinsci.plugins.octoperf.credentials.CredentialsService.CREDENTIALS_SERVICE;


@Slf4j
@Extension(optional = true)
public class OctoPerfBuilderDSLExtension extends ContextExtensionPoint {

  @DslExtensionMethod(context = StepContext.class)
  public Object octoPerfTest(final Runnable closure) {
    log.info("Running 'octoPerfTest' method from JOB DSL plugin...");

    final OctoPerfBuilderDSLContext context = new OctoPerfBuilderDSLContext();
    executeInContext(closure, context);

    final OctoperfBuilderDescriptor desc = OctoperfBuilderDescriptor.getDescriptor();
    final String serverUrl = desc.getOctoperfURL();

    OctoperfBuilder builder = null;
    try {

      final Optional<OctoperfCredential> credentials = CREDENTIALS_SERVICE.find(context.credentialsId, null);
      log.info(context.credentialsId + " is " + (credentials.isPresent() ? "" : "not") + " present in credentials");

      if (credentials.isPresent()) {
        builder = new OctoperfBuilder(
            context.credentialsId,
            "",
            "",
            context.scenarioId,
            context.getStopConditions()
        );
        builder.setServerUrl(serverUrl);
      }

    } catch (final Exception e) {
      log.warn("Failed to create OctoPerfBuilder object from Job DSL description: credentialsId=" + context.credentialsId +
          ", scenarioId =" + context.scenarioId + ", serverUrl=" + serverUrl);
    } finally {
      return builder;
    }
  }
}
