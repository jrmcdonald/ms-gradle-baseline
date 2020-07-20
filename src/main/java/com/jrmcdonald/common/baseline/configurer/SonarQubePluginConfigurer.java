package com.jrmcdonald.common.baseline.configurer;

import org.gradle.api.Project;
import org.sonarqube.gradle.SonarQubePlugin;

public class SonarQubePluginConfigurer implements PluginConfigurer {

    @Override
    public void apply(Project project) {
        if (project.equals(project.getRootProject())) {
            project.getPlugins().apply(SonarQubePlugin.class);
        }
    }
}
