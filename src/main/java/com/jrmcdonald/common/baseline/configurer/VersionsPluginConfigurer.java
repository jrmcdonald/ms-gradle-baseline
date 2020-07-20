package com.jrmcdonald.common.baseline.configurer;

import com.github.benmanes.gradle.versions.VersionsPlugin;

import org.gradle.api.Project;

public class VersionsPluginConfigurer implements PluginConfigurer {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(VersionsPlugin.class);
    }
}
