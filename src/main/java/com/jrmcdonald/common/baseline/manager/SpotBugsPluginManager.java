package com.jrmcdonald.common.baseline.manager;

import com.github.spotbugs.snom.SpotBugsPlugin;

import org.gradle.api.Project;

public class SpotBugsPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToJavaProject(project);
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(SpotBugsPlugin.class));
    }
}
