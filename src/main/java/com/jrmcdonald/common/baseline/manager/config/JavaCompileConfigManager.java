package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.compile.JavaCompile;

import java.util.Set;

public class JavaCompileConfigManager implements ConfigManager {

    private static final Set<String> COMPILE_OPTIONS = Set.of("-Werror", "-Xlint:all", "-Xlint:try", "-Xlint:-processing");

    @Override
    public void apply(Project project) {
        applyJavaCompileConfiguration(project);
    }

    private void applyJavaCompileConfiguration(Project project) {
        project.getPlugins()
               .withType(JavaPlugin.class, unused -> project.getTasks()
                                                            .withType(JavaCompile.class, task -> task.getOptions().getCompilerArgs().addAll(COMPILE_OPTIONS)));
    }
}
