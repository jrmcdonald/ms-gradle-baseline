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

        project.getExtensions().create("baseline", BaselinePluginExtension.class);
        project.allprojects(this::applyManagers);
        project.allprojects(this::afterEvaluateManagers);
    }

    private void applyManagers(Project project) {
        managers.forEach(manager -> manager.apply(project));
    }

    private void afterEvaluateManagers(Project project) {
        project.afterEvaluate(p -> managers.forEach(manager -> manager.afterEvaluate(p)));
    }

}
