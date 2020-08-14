package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.tasks.diagnostics.DependencyInsightReportTask;
import org.gradle.api.tasks.diagnostics.DependencyReportTask;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jrmcdonald.common.baseline.manager.config.DependencyInsightConfigManager.ALL_DEPS_TASK;
import static com.jrmcdonald.common.baseline.manager.config.DependencyInsightConfigManager.ALL_DEP_INSIGHT_TASK;
import static org.assertj.core.api.Assertions.assertThat;

class DependencyInsightConfigManagerTest {

    private Project rootProject;
    private Project intermediateProject;
    private Project subProject;

    private DependencyInsightConfigManager manager;

    @BeforeEach
    void beforeEach() {
        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        intermediateProject = ProjectBuilder.builder().withName("intermediateProject").withParent(rootProject).build();
        subProject = ProjectBuilder.builder().withName("subProject").withParent(intermediateProject).build();

        manager = new DependencyInsightConfigManager();
    }

    @Test
    @DisplayName("Should not apply to non root project")
    void shouldNotApplyToNonRootProject() {
        manager.apply(intermediateProject);

        assertThat(intermediateProject.getTasks().findByName(ALL_DEPS_TASK)).isNull();
        assertThat(intermediateProject.getTasks().findByName(ALL_DEP_INSIGHT_TASK)).isNull();
    }

    @Test
    @DisplayName("Should configure `allDeps`Task")
    void shouldConfigureAllDepsTask() {
        manager.apply(rootProject);

        assertThat(intermediateProject.getTasks().findByName(ALL_DEPS_TASK)).isInstanceOf(DependencyReportTask.class);
        assertThat(subProject.getTasks().findByName(ALL_DEPS_TASK)).isInstanceOf(DependencyReportTask.class);
    }

    @Test
    @DisplayName("Should configure `allDepInsight` task")
    void shouldConfigureAllDepInsightTask() {
        manager.apply(rootProject);

        assertThat(intermediateProject.getTasks().findByName(ALL_DEP_INSIGHT_TASK)).isInstanceOf(DependencyInsightReportTask.class);
        assertThat(subProject.getTasks().findByName(ALL_DEP_INSIGHT_TASK)).isInstanceOf(DependencyInsightReportTask.class);
    }

}