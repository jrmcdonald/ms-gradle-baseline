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
        var managers = List.of(new DependencyCheckPluginManager(),
                               new GitHooksPluginManager(),
                               new JacocoPluginManager(),
                               new SonarQubePluginManager(),
                               new SpotBugsPluginManager(),
                               new SpringBootPluginManager(),
                               new VersionsPluginManager());

        applyToProjectWithManagers(project, managers);
    }

    public static void applyToProjectWithManagers(Project project, List<? extends PluginManager> managers) {
        var root = project.getRootProject();
        if (!project.equals(root)) {
            throw new InvalidProjectTargetException("com.jrmcdonald.common.baseline should be applied to the root project only");
        }

        managers.forEach(manager -> root.allprojects(manager::apply));
    }

}
