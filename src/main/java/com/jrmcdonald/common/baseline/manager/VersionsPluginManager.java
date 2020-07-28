package com.jrmcdonald.common.baseline.manager;

import com.github.benmanes.gradle.versions.VersionsPlugin;
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask;
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentFilter;
import com.jrmcdonald.common.baseline.core.versions.ComponentFilterFactory;

import org.gradle.api.Project;

import lombok.Setter;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.CHECK;
import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getRootProjectPath;

public class VersionsPluginManager implements PluginManager {

    @Setter
    private ComponentFilter filter = ComponentFilterFactory.get();

    @Override
    public void apply(Project project) {
        applyToProject(project);
        configureDependencyUpdatesTask(project);
    }

    @Override
    public void afterEvaluate(Project project) {
        configureTaskOrdering(project);
    }

    public void applyToProject(Project project) {
        project.getPlugins().apply(VersionsPlugin.class);
    }

    public void configureDependencyUpdatesTask(Project project) {
        if (isRootProject(project)) {
            project.getTasks().withType(DependencyUpdatesTask.class, task -> {
                task.setCheckConstraints(true);
                task.rejectVersionIf(filter);
            });
        }
    }

    private void configureTaskOrdering(Project project) {
        if (isRootProject(project)) {
            project.getTasks().withType(DependencyUpdatesTask.class, task -> {
                var check = project.getTasks().findByPath(getRootProjectPath(CHECK));
                if (check != null) {
                    check.getDependsOn().add(task);
                }
            });
        }
    }
}
