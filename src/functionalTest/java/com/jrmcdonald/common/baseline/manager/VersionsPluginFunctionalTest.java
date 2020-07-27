package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionsPluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `dependencyUpdates` task")
    void canExecuteDependencyUpdatesTask() {
        var result = build("dependencyUpdates", "-m");

        assertThat(result.getOutput()).contains(":dependencyUpdates SKIPPED")
                                      .contains(BUILD_SUCCESSFUL);
    }

    @Test
    @DisplayName("Executing the `check` task executes the `dependencyUpdates` task")
    void executingTheCheckTaskExecutesTheDependencyUpdatesTask() {
        var result = build("check", "-m");

        assertThat(result.getOutput()).contains(":dependencyUpdates SKIPPED")
                                      .contains(BUILD_SUCCESSFUL);
    }
}
