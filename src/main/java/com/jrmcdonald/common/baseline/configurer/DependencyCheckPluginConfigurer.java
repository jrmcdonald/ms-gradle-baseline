package com.jrmcdonald.common.baseline.configurer;

import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;

public class DependencyCheckPluginConfigurer implements PluginConfigurer {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(DependencyCheckPlugin.class);
    }
}
