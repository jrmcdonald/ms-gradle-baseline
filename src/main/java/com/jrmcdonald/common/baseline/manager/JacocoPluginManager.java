package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Project;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;

public class JacocoPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToJavaProject(project);
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(JacocoPlugin.class));
    }
}
