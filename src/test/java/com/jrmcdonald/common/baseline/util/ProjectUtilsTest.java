package com.jrmcdonald.common.baseline.util;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectUtilsTest {

    private Project rootProject;
    private Project subProject;

    @BeforeEach
    void beforeEach() {
        rootProject = ProjectBuilder.builder().build();
        subProject = ProjectBuilder.builder().withParent(rootProject).build();
    }

    @Test
    @DisplayName("Should return true if root project")
    void shouldReturnTrueIfRootProject() {
        assertThat(isRootProject(rootProject)).isTrue();
    }

    @Test
    @DisplayName("Should return false if not root project")
    void shouldReturnFalseIfNotRootProject() { assertThat(isRootProject(subProject)).isFalse(); }
}