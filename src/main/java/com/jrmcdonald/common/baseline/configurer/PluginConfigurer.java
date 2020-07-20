package com.jrmcdonald.common.baseline.configurer;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public interface PluginConfigurer extends Plugin<Project> {

    default void apply(Project project) {}

}
