package com.jrmcdonald.common.baseline.plugin;

import com.jrmcdonald.common.baseline.configurer.DependencyCheckPluginConfigurer;
import com.jrmcdonald.common.baseline.configurer.GitHooksPluginConfigurer;
import com.jrmcdonald.common.baseline.configurer.JacocoPluginConfigurer;
import com.jrmcdonald.common.baseline.configurer.PluginConfigurer;
import com.jrmcdonald.common.baseline.configurer.SonarQubePluginConfigurer;
import com.jrmcdonald.common.baseline.configurer.SpotBugsPluginConfigurer;
import com.jrmcdonald.common.baseline.configurer.SpringBootPluginConfigurer;
import com.jrmcdonald.common.baseline.configurer.VersionsPluginConfigurer;
import com.jrmcdonald.common.baseline.exception.InvalidProjectTargetException;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.List;

public class BaselinePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        var configurers = List.of(DependencyCheckPluginConfigurer.class,
                                  GitHooksPluginConfigurer.class,
                                  JacocoPluginConfigurer.class,
                                  SonarQubePluginConfigurer.class,
                                  SpotBugsPluginConfigurer.class,
                                  SpringBootPluginConfigurer.class,
                                  VersionsPluginConfigurer.class);

        applyToProjectWithConfigurers(project, configurers);
    }

    public static void applyToProjectWithConfigurers(Project project, List<Class<? extends PluginConfigurer>> configurers) {
        var root = project.getRootProject();
        if (!project.equals(root)) {
            throw new InvalidProjectTargetException("com.jrmcdonald.common.baseline should be applied to the root project only");
        }

        root.allprojects(p -> configurers.forEach(configurer -> p.getPlugins()
                                                                 .apply(configurer)));
    }

}
