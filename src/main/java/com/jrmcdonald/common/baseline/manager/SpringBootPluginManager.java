package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.plugin.BaselinePluginExtension;

import org.gradle.api.Project;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;
import org.springframework.boot.gradle.tasks.bundling.BootJar;

import java.util.Objects;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.JAR;

public class SpringBootPluginManager implements PluginManager {

    @Override
    public void afterEvaluate(Project project) {
        var extension = Objects.requireNonNull(project.getRootProject().getExtensions().findByType(BaselinePluginExtension.class));
        if (extension.isSpringBootEnabled()) {
            applyToJavaProject(project);
            configurePlugin(project);
        }
    }

    public void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(SpringBootPlugin.class));
    }

    public void configurePlugin(Project project) {
        project.getPluginManager().withPlugin("org.springframework.boot", unused -> {
            disableBootJarTask(project);
            enableJarTask(project);
        });
    }

    private void disableBootJarTask(Project project) {
        project.getTasks().withType(BootJar.class, task -> task.setEnabled(false));
    }

    private void enableJarTask(Project project) {
        Objects.requireNonNull(project.getTasks().findByName(JAR)).setEnabled(true);
    }
}
