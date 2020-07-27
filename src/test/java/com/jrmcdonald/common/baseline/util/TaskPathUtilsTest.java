package com.jrmcdonald.common.baseline.util;

import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskPathUtilsTest {

    @Test
    @DisplayName("Should format path for root project")
    void shouldFormatPathForRootProject() {
        var path = TaskPathUtils.getRootProjectPath("a-task");
        assertThat(path).isEqualTo(":a-task");
    }

    @Test
    @DisplayName("Should format path for sub project")
    void shouldFormatPathForSubProject() {
        var project = ProjectBuilder.builder().withName("a-project").build();
        var path = TaskPathUtils.getSubProjectPath(project, "a-task");
        assertThat(path).isEqualTo(":a-project:a-task");
    }

}