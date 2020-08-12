package com.jrmcdonald.common.baseline.util;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.JACOCO_TEST_REPORT;
import static com.jrmcdonald.common.baseline.util.ProjectUtils.findTaskByName;
import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectUtilsTest {

    private Project rootProject;
    private Project subProject;

    @BeforeEach
    void beforeEach() {
        rootProject = ProjectBuilder.builder().build();
        subProject = ProjectBuilder.builder().withParent(rootProject).build();
    }

    @DisplayName("isRootProject Tests")
    @Nested
    class IsRootProjectTests {

        @Test
        @DisplayName("Should return true if root project")
        void shouldReturnTrueIfRootProject() {
            assertThat(isRootProject(rootProject)).isTrue();
        }

        @Test
        @DisplayName("Should return false if not root project")
        void shouldReturnFalseIfNotRootProject() { assertThat(isRootProject(subProject)).isFalse(); }

    }

    @DisplayName("findTaskByName Tests")
    @Nested
    class FindTaskByNameTests {

        @BeforeEach
        void beforeEach() {
            rootProject.getPlugins().apply(JavaPlugin.class);
            rootProject.getPlugins().apply(JacocoPlugin.class);
        }

        @Test
        @DisplayName("Should return type cast task by name")
        void shouldReturnTypeCastTaskByName() {
            assertThat(findTaskByName(rootProject, JACOCO_TEST_REPORT, JacocoReport.class)).isInstanceOf(JacocoReport.class);
        }

        @Test
        @DisplayName("Should throw NullPointerException if task is not found")
        void shouldThrowNullPointerExceptionIfTaskIsNotFound() {
            assertThatThrownBy(() -> findTaskByName(rootProject, "someRandomTask", Task.class)).isInstanceOf(NullPointerException.class);
        }
    }
}