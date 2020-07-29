package com.jrmcdonald.common.baseline.core.jacoco;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CodeCoverageReportTaskTest {

    @TempDir
    public File projectDir;

    private Project rootProject;

    @BeforeEach
    void beforeEach() {
        rootProject = ProjectBuilder.builder().withName("rootProject").withProjectDir(projectDir).build();
        rootProject.getTasks().register(CodeCoverageReportTask.NAME, CodeCoverageReportTask.class);

        var intermediateProject = ProjectBuilder.builder().withName("intermediateProject").withParent(rootProject).build();
        intermediateProject.getPlugins().apply(JavaPlugin.class);
        intermediateProject.getPlugins().apply(JacocoPlugin.class);

        var subProject = ProjectBuilder.builder().withName("subProject").withParent(intermediateProject).build();
        subProject.getPlugins().apply(JavaPlugin.class);
        subProject.getPlugins().apply(JacocoPlugin.class);

        var javaSrc = projectDir.toPath().resolve("intermediateProject/subProject/src/main/java");
        assertThat(javaSrc.toFile().mkdirs()).isTrue();

        // work around task configuration avoidance by calling eagerly loading the jacocoTestReport task
        // https://docs.gradle.org/current/userguide/task_configuration_avoidance.html
        // this ensures that the source sets get configured in the unit test
        intermediateProject.getTasks().getByName("jacocoTestReport");
        subProject.getTasks().getByName("jacocoTestReport");
    }


    @Test
    @DisplayName("Should not configure source sets for intermediate project")
    void shouldNotConfigureSourceSetsForIntermediateProject() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> {
            var fileSystemLocations = task.getSourceDirectories().getElements().get();
            assertThat(fileSystemLocations).noneMatch(fileSystemLocation -> fileSystemLocation.toString().contains("intermediateProject/src/main/java"));
        });
    }

    @Test
    @DisplayName("Should configure source sets for sub project")
    void shouldConfigureSourceSetsForSubProject() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> {
            var fileSystemLocations = task.getSourceDirectories().getElements().get();
            assertThat(fileSystemLocations).anyMatch(fileSystemLocation -> fileSystemLocation.toString().contains("intermediateProject/subProject/src/main/java"));
        });
    }

    @Test
    @DisplayName("Should not configure execution data for intermediate project")
    void shouldNotConfigureExecutionDataForIntermediateProject() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> {
            var fileSystemLocations = task.getExecutionData().getElements().get();
            assertThat(fileSystemLocations).noneMatch(fileSystemLocation -> fileSystemLocation.toString().contains("intermediateProject/build/jacoco/test.exe"));
        });
    }

    @Test
    @DisplayName("Should configure execution data for sub project")
    void shouldConfigureExecutionDataForSubProject() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> {
            var fileSystemLocations = task.getExecutionData().getElements().get();
            assertThat(fileSystemLocations).anyMatch(fileSystemLocation -> fileSystemLocation.toString().contains("intermediateProject/subProject/build/jacoco/test.exe"));
        });
    }

}