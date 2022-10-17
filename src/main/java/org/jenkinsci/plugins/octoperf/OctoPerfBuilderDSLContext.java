package org.jenkinsci.plugins.octoperf;

import javaposse.jobdsl.dsl.Context;
import lombok.Data;
import org.jenkinsci.plugins.octoperf.conditions.TestStopCondition;

import java.util.ArrayList;
import java.util.List;

@Data
public class OctoPerfBuilderDSLContext implements Context {
    String credentialsId = "";
    String scenarioId = "";
    String testName = "";
    List<? extends TestStopCondition> stopConditions = new ArrayList<>();
}
