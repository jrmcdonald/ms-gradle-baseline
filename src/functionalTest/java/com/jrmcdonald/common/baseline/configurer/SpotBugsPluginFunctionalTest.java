package com.jrmcdonald.common.baseline.configurer;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpotBugsPluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `spotbugsMain` task")
    void canExecuteSpotbugsMainTask() {
        var result = build("spotbugsMain", "-m");

        assertThat(result.getOutput()).contains(":spotbugsMain SKIPPED")
                                      .contains("BUILD SUCCESSFUL");
    }
}
