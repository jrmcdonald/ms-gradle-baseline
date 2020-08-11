package com.jrmcdonald.common.baseline.plugin;

import com.jrmcdonald.common.baseline.exception.InvalidProjectTargetException;
import com.jrmcdonald.common.baseline.manager.plugin.DependencyCheckPluginManager;
import com.jrmcdonald.common.baseline.manager.plugin.GitHooksPluginManager;
import com.jrmcdonald.common.baseline.manager.plugin.JacocoPluginManager;
import com.jrmcdonald.common.baseline.manager.plugin.PluginManager;
import com.jrmcdonald.common.baseline.manager.plugin.SonarQubePluginManager;
import com.jrmcdonald.common.baseline.manager.plugin.SpotBugsPluginManager;
import com.jrmcdonald.common.baseline.manager.plugin.SpringBootPluginManager;
import com.jrmcdonald.common.baseline.manager.plugin.VersionsPluginManager;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.List;

import lombok.Setter;

public class BaselinePlugin implements Plugin<Project> {

    @Setter
    private List<PluginManager> managers;

    public BaselinePlugin() {
        managers = List.of(new DependencyCheckPluginManager(),
                           new GitHooksPluginManager(),
                           new JacocoPluginManager(),
                           new SonarQubePluginManager(),
                           new SpotBugsPluginManager(),
                           new SpringBootPluginManager(),
                           new VersionsPluginManager());
    }

    @Override
    public void apply(Project project) {
        var rootProject = project.getRootProject();
        if (!project.equals(rootProject)) {
            throw new InvalidProjectTargetException("com.jrmcdonald.common.baseline should be applied to the root project only");
        }

        rootProject.allprojects(this::applyManagers);
        rootProject.allprojects(this::afterEvaluateManagers);
    }

    private void applyManagers(Project project) {
        managers.forEach(manager -> manager.apply(project));
    }

    private void afterEvaluateManagers(Project project) {
        project.afterEvaluate(p -> managers.forEach(manager -> manager.afterEvaluate(p)));
    }

}
