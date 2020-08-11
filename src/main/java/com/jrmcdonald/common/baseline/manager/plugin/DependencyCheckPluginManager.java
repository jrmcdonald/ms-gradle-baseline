package com.jrmcdonald.common.baseline.manager.plugin;

import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;
import org.owasp.dependencycheck.gradle.tasks.Aggregate;
import org.owasp.dependencycheck.reporting.ReportGenerator.Format;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.BUILD;
import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getRootProjectPath;

public class DependencyCheckPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToProject(project);
        configureExtension(project);
    }

    @Override
    public void afterEvaluate(Project project) {
        configureTaskOrdering(project);
    }

    public void applyToProject(Project project) {
        project.getPlugins().apply(DependencyCheckPlugin.class);
    }

    private void configureExtension(Project project) {
        if (isRootProject(project)) {
            var extension = project.getExtensions().findByType(DependencyCheckExtension.class);
            if (extension != null) {
                extension.setFormat(Format.ALL);
                extension.getAnalyzers().setNodeEnabled(false);
            }
        }
    }

    private void configureTaskOrdering(Project project) {
        if (isRootProject(project)) {
            project.getTasks().withType(Aggregate.class, task -> {
                var build = project.getTasks().findByPath(getRootProjectPath(BUILD));
                if (build != null) {
                    build.getDependsOn().add(task);
                }
            });
        }
    }
}
