package com.jrmcdonald.common.baseline.manager.plugin;

import com.github.spotbugs.snom.SpotBugsPlugin;
import com.github.spotbugs.snom.SpotBugsTask;

import org.gradle.api.Project;

public class SpotBugsPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToJavaProject(project);
        configureJavaProject(project);
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(SpotBugsPlugin.class));
    }

    public void configureJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused ->
                project.getTasks().withType(SpotBugsTask.class).forEach(task -> {
                    task.getReports().create("xml").setEnabled(false);
                    task.getReports().create("html").setEnabled(true);
                }));
    }
}
