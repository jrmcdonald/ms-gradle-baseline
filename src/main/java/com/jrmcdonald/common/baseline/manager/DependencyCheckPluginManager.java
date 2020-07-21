package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;

public class DependencyCheckPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(DependencyCheckPlugin.class);
    }
}
