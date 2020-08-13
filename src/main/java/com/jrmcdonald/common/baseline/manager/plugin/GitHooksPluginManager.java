package com.jrmcdonald.common.baseline.manager.plugin;

import com.gtramontina.ghooks.GHooks;

import org.gradle.api.Project;

import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;

public class GitHooksPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToRootProject(project);
    }

    private void applyToRootProject(Project project) {
        if (isRootProject(project)) {
            project.getPlugins().apply(GHooks.class);
        }
    }
}
