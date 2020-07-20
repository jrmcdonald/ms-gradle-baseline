package com.jrmcdonald.common.baseline.configurer;

import org.gradle.api.Project;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

public class SpringBootPluginConfigurer implements PluginConfigurer {

    @Override
    public void apply(Project project) {
        applyToJavaProject(project);
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> {
            project.getPluginManager().apply(SpringBootPlugin.class);
        });
    }
}
