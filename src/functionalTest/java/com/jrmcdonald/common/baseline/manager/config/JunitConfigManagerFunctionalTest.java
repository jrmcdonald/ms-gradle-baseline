package com.jrmcdonald.common.baseline.manager.config;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class JunitConfigManagerFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute JUnit 5 unit tests")
    void canExecuteJUnit5UnitTests() {
        appendToProject(DEPENDENCIES_FILE, BUILD_GRADLE);
        copyToProject(APPLICATION_FILE, "src/main/java/com/jrmcdonald/baseline/Application.java");
        copyToProject(APPLICATION_TEST_FILE, "src/test/java/com/jrmcdonald/baseline/ApplicationTest.java");

        var result = build("build", "-x", "dependencyUpdates", "-x", "dependencyCheckAggregate");

        assertThat(result.getOutput()).contains(BUILD_SUCCESSFUL);
        assertThat(new File(projectDir, "build/reports/tests/test/index.html")).exists();
    }

}
