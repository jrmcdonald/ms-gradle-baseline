package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyCheckPluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `dependencyUpdates` task")
    void canExecuteDependencyUpdatesTask() {
        var result = build("dependencyCheckAnalyze", "-m");

        assertThat(result.getOutput()).contains(":dependencyCheckAnalyze SKIPPED")
                                      .contains("BUILD SUCCESSFUL");
    }
}
