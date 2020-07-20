package com.jrmcdonald.common.baseline.configurer;

import com.gtramontina.ghooks.GHooks;

import org.gradle.api.Project;

public class GitHooksPluginConfigurer implements PluginConfigurer {

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
