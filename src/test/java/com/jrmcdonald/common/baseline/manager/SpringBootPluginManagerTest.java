package com.jrmcdonald.common.baseline.manager;

import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBootPluginManagerTest {

    @Test
    @DisplayName("Should apply plugins to java projects")
    void shouldApplyPluginsToJavaProjects() {
        var project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        project.getPlugins().apply(SpringBootPluginManager.class);

        assertThat(project.getPlugins().hasPlugin(SpringBootPluginManager.class)).isTrue();
        assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class)).isTrue();
    }

    @Test
    @DisplayName("Should not apply expected plugins to non java projects")
    void shouldNotApplyPluginsToNonJavaProjects() {
        var project = ProjectBuilder.builder().build();
        project.getPlugins().apply(SpringBootPluginManager.class);

        assertThat(project.getPlugins().hasPlugin(SpringBootPluginManager.class)).isTrue();
        assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class)).isFalse();
    }
}