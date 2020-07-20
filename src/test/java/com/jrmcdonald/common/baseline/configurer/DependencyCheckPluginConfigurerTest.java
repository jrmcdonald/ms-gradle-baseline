package com.jrmcdonald.common.baseline.configurer;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyCheckPluginConfigurerTest {

    private Project project;

    @BeforeEach
    void beforeEach() {
        project = ProjectBuilder.builder().build();
        project.getPlugins().apply(DependencyCheckPluginConfigurer.class);
    }

    @Test
    @DisplayName("Should apply plugins to project")
    void shouldApplyPluginsToProject() {
        assertThat(project.getPlugins().hasPlugin(DependencyCheckPluginConfigurer.class)).isTrue();
        assertThat(project.getPlugins().hasPlugin(DependencyCheckPlugin.class)).isTrue();
    }
}