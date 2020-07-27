package com.jrmcdonald.common.baseline.core.jacoco;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JacocoUtilsTest {

    Project project;

    @BeforeEach
    void beforeEach() {
        project = ProjectBuilder.builder().build();
        project.getPlugins().apply(JavaPlugin.class);
        project.getPlugins().apply(JacocoPlugin.class);
    }

    @Test
    @DisplayName("Should return true when `JacocoTaskExtension` is present")
    void shouldReturnTrueWhenJacocoTaskExtensionIsPresent() {
        var task = project.getTasks().getByName("test");
        assertThat(JacocoUtils.hasJacocoTaskExtension(task)).isTrue();
    }

    @Test
    @DisplayName("Should return false when `JacocoTaskExtension` is not present")
    void shouldReturnFalseWhenJacocoTaskExtensionIsNotPresent() {
        var task = project.getTasks().getByName("build");
        assertThat(JacocoUtils.hasJacocoTaskExtension(task)).isFalse();
    }
}