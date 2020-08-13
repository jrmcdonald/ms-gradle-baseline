package com.jrmcdonald.common.baseline.manager.plugin;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JacocoPluginFunctionalTest extends AbstractGradleFunctionalTest {

    private static final String CODE_COVERAGE_REPORT_SKIPPED = ":codeCoverageReport SKIPPED";

    @Test
    @DisplayName("Can execute `jacocoTestReport` task")
    void canExecuteJacocoTestReportTask() {
        var result = build("jacocoTestReport", "-m");

        assertThat(result.getOutput()).contains(":jacocoTestReport SKIPPED")
                                      .contains(BUILD_SUCCESSFUL);
    }

    @Test
    @DisplayName("Can execute `codeCoverageReport` task")
    void canExecuteCodeCoverageReportTask() {
        var result = build("codeCoverageReport", "-m");

        assertThat(result.getOutput()).contains(CODE_COVERAGE_REPORT_SKIPPED)
                                      .contains(BUILD_SUCCESSFUL);
    }

    @Test
    @DisplayName("Executing the `test` task executes the `codeCoverageReport` task")
    void executingTheTestTaskExecutesTheCodeCoverageReportTask() {
        var result = build("test", "-m");

        assertThat(result.getOutput()).contains(CODE_COVERAGE_REPORT_SKIPPED)
                                      .contains(BUILD_SUCCESSFUL);
    }

    @Test
    @DisplayName("Executing the `check` task executes the `codeCoverageReport` task")
    void executingTheCheckTaskExecutesTheCodeCoverageReportTask() {
        var result = build("check", "-m");

        assertThat(result.getOutput()).contains(CODE_COVERAGE_REPORT_SKIPPED)
                                      .contains(BUILD_SUCCESSFUL);
    }
}
