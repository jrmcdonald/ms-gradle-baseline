package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBootPluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `bootRun` task")
    void canExecuteBootRunTask() {
        var result = build("bootRun", "-m");

        assertThat(result.getOutput()).contains(":bootRun SKIPPED")
                                      .contains(BUILD_SUCCESSFUL);
    }

    @Test
    @DisplayName("Cannot execute `bootRun` task when `springBootEnabled` is false")
    void cannotExecuteBootRunTaskWhenSpringBootEnabledIsFalse() {
        appendToProject("src/functionalTest/resources/snippets/baseline-spring-boot-disabled.gradle", BUILD_GRADLE);

        var result = buildAndFail("bootRun", "-m");

        assertThat(result.getOutput()).contains("Task 'bootRun' not found in root project");
    }

}
