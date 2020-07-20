package com.jrmcdonald.common.baseline.configurer;

import com.gtramontina.ghooks.GHooks;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GitHooksPluginConfigurerTest {

    Project rootProject;
    Project subProject;

    @BeforeEach
    void beforeEach() {
        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();

        List.of(rootProject, subProject).forEach(project -> project.getPlugins().apply(GitHooksPluginConfigurer.class));
    }

    @Test
    @DisplayName("Should apply plugins to root project")
    void shouldApplyPluginsToRootProject() {
        assertThat(rootProject.getPlugins().hasPlugin(GitHooksPluginConfigurer.class)).isTrue();
        assertThat(rootProject.getPlugins().hasPlugin(GHooks.class)).isTrue();
    }

    @Test
    @DisplayName("Should not apply plugins to non root project")
    void shouldNotApplyPluginsToNonRootProject() {
        assertThat(subProject.getPlugins().hasPlugin(GitHooksPluginConfigurer.class)).isTrue();
        assertThat(subProject.getPlugins().hasPlugin(GHooks.class)).isFalse();
    }

}