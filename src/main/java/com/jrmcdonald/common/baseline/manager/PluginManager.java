package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Project;

public interface PluginManager {

    default void apply(Project project) {}

    default void afterEvaluate(Project project) {}

}
