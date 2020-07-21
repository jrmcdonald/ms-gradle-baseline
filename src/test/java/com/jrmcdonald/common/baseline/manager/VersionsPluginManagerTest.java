package com.jrmcdonald.common.baseline.manager;

import com.github.benmanes.gradle.versions.VersionsPlugin;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionsPluginManagerTest {

    private Project project;

    @BeforeEach
    void beforeEach() {
        project = ProjectBuilder.builder().build();
        project.getPlugins().apply(VersionsPluginManager.class);
    }

    @Test
    @DisplayName("Should apply plugins to project")
    void shouldApplyPluginsToProject() {
        assertThat(project.getPlugins().hasPlugin(VersionsPluginManager.class)).isTrue();
        assertThat(project.getPlugins().hasPlugin(VersionsPlugin.class)).isTrue();
    }

}