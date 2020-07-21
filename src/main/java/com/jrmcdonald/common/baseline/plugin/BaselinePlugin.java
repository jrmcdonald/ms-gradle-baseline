package com.jrmcdonald.common.baseline.plugin;

import com.jrmcdonald.common.baseline.exception.InvalidProjectTargetException;
import com.jrmcdonald.common.baseline.manager.DependencyCheckPluginManager;
import com.jrmcdonald.common.baseline.manager.GitHooksPluginManager;
import com.jrmcdonald.common.baseline.manager.JacocoPluginManager;
import com.jrmcdonald.common.baseline.manager.PluginManager;
import com.jrmcdonald.common.baseline.manager.SonarQubePluginManager;
import com.jrmcdonald.common.baseline.manager.SpotBugsPluginManager;
import com.jrmcdonald.common.baseline.manager.SpringBootPluginManager;
import com.jrmcdonald.common.baseline.manager.VersionsPluginManager;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.List;

public class BaselinePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        var manager = List.of(DependencyCheckPluginManager.class,
                              GitHooksPluginManager.class,
                              JacocoPluginManager.class,
                              SonarQubePluginManager.class,
                              SpotBugsPluginManager.class,
                              SpringBootPluginManager.class,
                              VersionsPluginManager.class);

        applyToProjectWithManagers(project, manager);
    }

    public static void applyToProjectWithManagers(Project project, List<Class<? extends PluginManager>> managers) {
        var root = project.getRootProject();
        if (!project.equals(root)) {
            throw new InvalidProjectTargetException("com.jrmcdonald.common.baseline should be applied to the root project only");
        }

        root.allprojects(p -> managers.forEach(manager -> p.getPlugins()
                                                           .apply(manager)));
    }

}
