package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;
import org.springframework.boot.gradle.tasks.bundling.BootJar;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBootPluginManagerTest {

    private SpringBootPluginManager manager;
    private Project project;

    @BeforeEach
    void beforeEach() {
        manager = new SpringBootPluginManager();

        project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
    }

    @Test
    @DisplayName("Should apply plugins to java projects")
    void shouldApplyPluginsToJavaProjects() {
        manager.apply(project);

        assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class)).isTrue();
    }

    @Test
    @DisplayName("Should not apply expected plugins to non java projects")
    void shouldNotApplyPluginsToNonJavaProjects() {
        var nonJavaProject = ProjectBuilder.builder().build();

        manager.apply(nonJavaProject);

        assertThat(nonJavaProject.getPlugins().hasPlugin(SpringBootPlugin.class)).isFalse();
    }

    @Test
    @DisplayName("Should disable the `bootJar` task by default")
    void shouldDisableTheBootJarTaskByDefault() {
        manager.apply(project);

        project.getTasks().withType(BootJar.class, task -> assertThat(task.isEnabled()).isFalse());
    }
}