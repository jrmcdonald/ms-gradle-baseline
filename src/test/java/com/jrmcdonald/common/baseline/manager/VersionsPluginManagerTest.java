package com.jrmcdonald.common.baseline.manager;

import com.github.benmanes.gradle.versions.VersionsPlugin;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionsPluginManagerTest {

    private VersionsPluginManager manager;
    private Project project;

    @BeforeEach
    void beforeEach() {
        manager = new VersionsPluginManager();
        project = ProjectBuilder.builder().build();

        manager.apply(project);
    }

    @Test
    @DisplayName("Should apply plugins to project")
    void shouldApplyPluginsToProject() {
        assertThat(project.getPlugins().hasPlugin(VersionsPlugin.class)).isTrue();
    }

}