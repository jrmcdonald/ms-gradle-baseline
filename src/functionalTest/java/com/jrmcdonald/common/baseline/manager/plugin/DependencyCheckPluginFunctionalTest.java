package com.jrmcdonald.common.baseline.manager.plugin;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyCheckPluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `dependencyCheckAnalyze` task")
    void canExecuteDependencyUpdatesTask() {
        var result = build("dependencyCheckAnalyze", "-m");

        assertThat(result.getOutput()).contains(":dependencyCheckAnalyze SKIPPED")
                                      .contains(BUILD_SUCCESSFUL);
    }

    @Test
    @DisplayName("Executing the `build` task triggers the `dependencyCheckAggregate` task")
    void executingTheBuildTaskTriggersTheDependencyCheckAggregateTask() {

        var result = build("build", "-m");

        assertThat(result.getOutput()).contains(":dependencyCheckAggregate SKIPPED")
                                      .contains(BUILD_SUCCESSFUL);
    }
}
