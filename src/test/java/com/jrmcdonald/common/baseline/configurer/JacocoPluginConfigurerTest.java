package com.jrmcdonald.common.baseline.configurer;

import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JacocoPluginConfigurerTest {

    @Test
    @DisplayName("Should apply plugins to java projects")
    void shouldApplyPluginsToJavaProjects() {
        var project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        project.getPlugins().apply(JacocoPluginConfigurer.class);

        assertThat(project.getPlugins().hasPlugin(JacocoPluginConfigurer.class)).isTrue();
        assertThat(project.getPlugins().hasPlugin(JacocoPlugin.class)).isTrue();
    }

    @Test
    @DisplayName("Should not apply expected plugins to non java projects")
    void shouldNotApplyPluginsToNonJavaProjects() {
        var project = ProjectBuilder.builder().build();
        project.getPlugins().apply(JacocoPluginConfigurer.class);

        assertThat(project.getPlugins().hasPlugin(JacocoPluginConfigurer.class)).isTrue();
        assertThat(project.getPlugins().hasPlugin(JacocoPlugin.class)).isFalse();
    }

}