package com.jrmcdonald.common.baseline.manager.plugin;

import org.gradle.api.Project;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;
import org.springframework.boot.gradle.tasks.bundling.BootJar;

public class SpringBootPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToJavaProject(project);
        configureJavaProject(project);
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(SpringBootPlugin.class));
    }

    private void configureJavaProject(Project project) {
        project.getTasks().withType(BootJar.class, task -> task.setEnabled(false));
    }
}
