package com.jrmcdonald.common.baseline.plugin;

import com.jrmcdonald.common.baseline.exception.InvalidProjectTargetException;
import com.jrmcdonald.common.baseline.manager.PluginManager;

import org.gradle.api.Plugin;
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

    private static final List<? extends PluginManager> TEST_MANAGERS = List.of(new TestPluginManager());

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
        BaselinePlugin.applyToProjectWithManagers(rootProject, TEST_MANAGERS);

        assertThat(rootProject.getPlugins().hasPlugin(TestPlugin.class)).isTrue();
        assertThat(subProject.getPlugins().hasPlugin(TestPlugin.class)).isTrue();
    }

    @Test
    @DisplayName("Should throw InvalidProjectTargetException when applied to non root project")
    void shouldThrowInvalidProjectTargetExceptionWhenAppliedToNonRootProject() {
        var exception = assertThrows(InvalidProjectTargetException.class, () -> BaselinePlugin.applyToProjectWithManagers(subProject, TEST_MANAGERS));

        assertThat(exception).hasMessage("com.jrmcdonald.common.baseline should be applied to the root project only");
    }

    public static class TestPluginManager implements PluginManager {

        @Override
        public void apply(Project project) {
            project.getPlugins().apply(TestPlugin.class);
        }
    }

    public static class TestPlugin implements Plugin<Project> {

        @Override
        public void apply(Project project) {
            // do nothing intentionally
        }
    }

}