package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.testing.Test;

public class JunitConfigManager implements ConfigManager {

    @Override
    public void apply(Project project) {
        applyJunitConfiguration(project);
    }

    private void applyJunitConfiguration(Project project) {
        project.getPlugins().withType(JavaPlugin.class, unused -> project.getTasks().withType(Test.class, Test::useJUnitPlatform));
    }
}
