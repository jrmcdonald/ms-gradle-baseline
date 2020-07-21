package com.jrmcdonald.common.baseline.manager;

import com.github.benmanes.gradle.versions.VersionsPlugin;

import org.gradle.api.Project;

public class VersionsPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(VersionsPlugin.class);
    }
}
