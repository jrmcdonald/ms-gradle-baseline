package com.jrmcdonald.common.baseline.configurer;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SonarQubePluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `sonarqube` task")
    void canExecuteSonarqubeTask() {
        var result = build("sonarqube", "-m");

        assertThat(result.getOutput()).contains(":sonarqube SKIPPED")
                                      .contains("BUILD SUCCESSFUL");
    }
}
