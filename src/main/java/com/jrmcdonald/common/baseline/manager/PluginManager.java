package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public interface PluginManager extends Plugin<Project> {

    @Override
    default void apply(Project project) {}

}
