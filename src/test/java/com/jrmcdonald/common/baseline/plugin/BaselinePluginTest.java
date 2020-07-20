package com.jrmcdonald.common.baseline.plugin;

import com.jrmcdonald.common.baseline.configurer.PluginConfigurer;
import com.jrmcdonald.common.baseline.exception.InvalidProjectTargetException;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class BaselinePluginTest {

    private static final List<Class<? extends PluginConfigurer>> TEST_CONFIGURERS = List.of(TestPluginConfigurer.class);

    private Project rootProject;
    private Project subProject;

    @BeforeEach
    void beforeEach() {
        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
    }

    @Test
    @DisplayName("Should apply plugins to all projects")
    void shouldApplyPluginsToAllProjects() {
        BaselinePlugin.applyToProjectWithConfigurers(rootProject, TEST_CONFIGURERS);

        assertThat(rootProject.getPlugins().hasPlugin(TestPluginConfigurer.class)).isTrue();
        assertThat(subProject.getPlugins().hasPlugin(TestPluginConfigurer.class)).isTrue();
    }

    @Test
    @DisplayName("Should throw InvalidProjectTargetException when applied to non root project")
    void shouldThrowInvalidProjectTargetExceptionWhenAppliedToNonRootProject() {
        var exception = assertThrows(InvalidProjectTargetException.class, () -> BaselinePlugin.applyToProjectWithConfigurers(subProject, TEST_CONFIGURERS));

        assertThat(exception).hasMessage("com.jrmcdonald.common.baseline should be applied to the root project only");
    }

    public static class TestPluginConfigurer implements PluginConfigurer {

        @Override
        public void apply(Project project) {
            // do nothing intentionally
        }
    }

}