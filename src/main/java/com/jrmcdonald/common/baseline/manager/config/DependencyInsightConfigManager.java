package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.tasks.diagnostics.DependencyInsightReportTask;
import org.gradle.api.tasks.diagnostics.DependencyReportTask;

import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;

public class DependencyInsightConfigManager implements ConfigManager {

    public static final String ALL_DEPS_TASK = "allDeps";
    public static final String ALL_DEP_INSIGHT_TASK = "allDepInsight";

    @Override
    public void apply(Project project) {
        if (isRootProject(project)) {
            project.getSubprojects().forEach(subProject -> {
                subProject.getTasks().create(ALL_DEPS_TASK, DependencyReportTask.class);
                subProject.getTasks().create(ALL_DEP_INSIGHT_TASK, DependencyInsightReportTask.class);
            });
        }
    }
}
