package org.jenkinsci.plugins.octoperf;

import javaposse.jobdsl.dsl.Context;
import lombok.Data;

@Data
public class OctoPerfBuilderDSLContext implements Context {
    String credentialsId = "";
    String scenarioId = "";
}
