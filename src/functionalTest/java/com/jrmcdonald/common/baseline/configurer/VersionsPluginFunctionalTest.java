package com.jrmcdonald.common.baseline.configurer;

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
                                      .contains("BUILD SUCCESSFUL");
    }
}
