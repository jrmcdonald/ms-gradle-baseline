package com.jrmcdonald.common.baseline.plugin;

import com.jrmcdonald.common.baseline.exception.InvalidProjectTargetException;
import com.jrmcdonald.common.baseline.manager.PluginManager;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
class BaselinePluginTest {

    @Mock
    private PluginManager manager;

    private BaselinePlugin plugin;

    private Project rootProject;
    private Project subProject;

    @BeforeEach
    void beforeEach() {
        plugin = new BaselinePlugin();
        plugin.setManagers(List.of(manager));

        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(manager);
    }

    @Test
    @DisplayName("Should execute manager")
    void shouldCallApplyOnManager() {
        plugin.apply(rootProject);

        // force afterEvaluate hook to be called
        rootProject.getTasksByName("tasks", false);
        subProject.getTasksByName("tasks", false);

        verify(manager).apply(rootProject);
        verify(manager).apply(subProject);

        verify(manager).afterEvaluate(rootProject);
        verify(manager).afterEvaluate(subProject);
    }

    @Test
    @DisplayName("Should throw InvalidProjectTargetException when applied to non root project")
    void shouldThrowInvalidProjectTargetExceptionWhenAppliedToNonRootProject() {
        var exception = assertThrows(InvalidProjectTargetException.class, () -> plugin.apply(subProject));

        assertThat(exception).hasMessage("com.jrmcdonald.common.baseline should be applied to the root project only");
    }

}