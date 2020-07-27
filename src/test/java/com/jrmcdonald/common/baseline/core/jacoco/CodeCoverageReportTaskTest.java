package com.jrmcdonald.common.baseline.core.jacoco;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodeCoverageReportTaskTest {

    private Project rootProject;

    @BeforeEach
    void beforeEach() {
        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        rootProject.getTasks().register(CodeCoverageReportTask.NAME, CodeCoverageReportTask.class);

        var subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
        subProject.getPlugins().apply(JavaPlugin.class);
        subProject.getPlugins().apply(JacocoPlugin.class);

        // work around task configuration avoidance by calling eagerly loading the jacocoTestReport task
        // https://docs.gradle.org/current/userguide/task_configuration_avoidance.html
        // this ensures that the source sets get configured in the unit test
        subProject.getTasks().getByName("jacocoTestReport");
    }

    @Test
    @DisplayName("Should enable html reports")
    void shouldEnableHtmlReports() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> assertThat(task.getReports().getHtml().isEnabled()).isTrue());
    }

    @Test
    @DisplayName("Should enable xml reports")
    void shouldEnableXmlReports() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> assertThat(task.getReports().getXml().isEnabled()).isTrue());
    }

    @Test
    @DisplayName("Should configure source sets")
    void shouldConfigureSourceSets() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> {
            var fileSystemLocations = task.getSourceDirectories().getElements().get();
            assertThat(fileSystemLocations).anyMatch(fileSystemLocation -> fileSystemLocation.toString().contains("subProject/src/main/java"));
        });
    }

    @Test
    @DisplayName("Should configure execution data")
    void shouldConfigureExecutionData() {
        rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> {
            var fileSystemLocations = task.getExecutionData().getElements().get();
            assertThat(fileSystemLocations).anyMatch(fileSystemLocation -> fileSystemLocation.toString().contains("subProject/build/jacoco/test.exe"));
        });
    }

}