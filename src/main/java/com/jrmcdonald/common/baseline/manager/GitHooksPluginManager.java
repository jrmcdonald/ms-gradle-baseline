package com.jrmcdonald.common.baseline.manager;

import com.gtramontina.ghooks.GHooks;

import org.gradle.api.Project;

public class GitHooksPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToRootProject(project);
    }

    private void applyToRootProject(Project project) {
        if (project.equals(project.getRootProject())) {
            project.getPlugins().apply(GHooks.class);
        }
    }
}
