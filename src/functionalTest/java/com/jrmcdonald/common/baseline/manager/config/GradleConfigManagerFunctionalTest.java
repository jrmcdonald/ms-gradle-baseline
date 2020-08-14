package com.jrmcdonald.common.baseline.manager.config;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GradleConfigManagerFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can use `commonPlatform` configuration")
    void canUseCommonPlatformConfiguration() {
            appendToProject(DEPENDENCIES_FILE, BUILD_GRADLE);

            var result = build("build", "-m");

            assertThat(result.getOutput()).contains(BUILD_SUCCESSFUL);
    }

}
