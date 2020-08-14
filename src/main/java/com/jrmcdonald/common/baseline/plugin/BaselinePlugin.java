package com.jrmcdonald.common.baseline.plugin;

import com.jrmcdonald.common.baseline.exception.InvalidProjectTargetException;
import com.jrmcdonald.common.baseline.manager.config.ConfigManager;
import com.jrmcdonald.common.baseline.manager.config.DependencyInsightConfigManager;
import com.jrmcdonald.common.baseline.manager.config.GradleConfigManager;
import com.jrmcdonald.common.baseline.manager.config.JavaCompileConfigManager;
import com.jrmcdonald.common.baseline.manager.config.JunitConfigManager;
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
    private List<PluginManager> pluginManagers;

    @Setter
    private List<ConfigManager> configManagers;

    public BaselinePlugin() {
        pluginManagers = List.of(new DependencyCheckPluginManager(),
                                 new GitHooksPluginManager(),
                                 new JacocoPluginManager(),
                                 new SonarQubePluginManager(),
                                 new SpotBugsPluginManager(),
                                 new SpringBootPluginManager(),
                                 new VersionsPluginManager());

        configManagers = List.of(new DependencyInsightConfigManager(),
                                 new GradleConfigManager(),
                                 new JavaCompileConfigManager(),
                                 new JunitConfigManager());
    }

    @Override
    public void apply(Project project) {
        var rootProject = project.getRootProject();
        if (!project.equals(rootProject)) {
            throw new InvalidProjectTargetException("com.jrmcdonald.common.baseline should be applied to the root project only");
        }

        rootProject.allprojects(this::applyPluginManagers);
        rootProject.allprojects(this::applyConfigManagers);

        rootProject.allprojects(this::afterEvaluatePluginManagers);
    }

    private void applyPluginManagers(Project project) {
        pluginManagers.forEach(manager -> manager.apply(project));
    }

    private void afterEvaluatePluginManagers(Project project) {
        project.afterEvaluate(p -> pluginManagers.forEach(manager -> manager.afterEvaluate(p)));
    }

    private void applyConfigManagers(Project project) { configManagers.forEach(configManager -> configManager.apply(project)); }

}
