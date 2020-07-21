package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JacocoPluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `jacocoTestReport` task")
    void canExecuteJacocoTestReportTask() {
        var result = build("jacocoTestReport", "-m");

        assertThat(result.getOutput()).contains(":jacocoTestReport SKIPPED")
                                      .contains("BUILD SUCCESSFUL");
    }
}
