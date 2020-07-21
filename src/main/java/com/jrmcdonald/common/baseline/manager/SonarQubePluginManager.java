package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Project;
import org.sonarqube.gradle.SonarQubePlugin;

public class SonarQubePluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        if (project.equals(project.getRootProject())) {
            project.getPlugins().apply(SonarQubePlugin.class);
        }
    }
}
