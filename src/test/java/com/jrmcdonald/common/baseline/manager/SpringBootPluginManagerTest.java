package com.jrmcdonald.common.baseline.manager;

import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBootPluginManagerTest {

    private SpringBootPluginManager manager;

    @BeforeEach
    void beforeEach() {
        manager = new SpringBootPluginManager();
    }

    @Test
    @DisplayName("Should apply plugins to java projects")
    void shouldApplyPluginsToJavaProjects() {
        var project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");

        manager.apply(project);

        assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class)).isTrue();
    }

    @Test
    @DisplayName("Should not apply expected plugins to non java projects")
    void shouldNotApplyPluginsToNonJavaProjects() {
        var project = ProjectBuilder.builder().build();

        manager.apply(project);

        assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class)).isFalse();
    }
}