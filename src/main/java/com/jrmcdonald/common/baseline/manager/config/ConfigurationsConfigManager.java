package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;

import java.util.Set;

public class ConfigurationsConfigManager implements ConfigManager {

    public static final String COMMON_PLATFORM_CONFIGURATION = "commonPlatform";

    private static final Set<String> JAVA_CONFIGURATIONS = Set.of("compileOnly",
                                                                  "annotationProcessor",
                                                                  "testCompileOnly",
                                                                  "testAnnotationProcessor",
                                                                  "implementation",
                                                                  "testFixturesImplementation");

    private static final Set<String> JAVA_LIBRARY_CONFIGURATIONS = Set.of("api");

    private static final Set<String> JAVA_TEST_FIXTURES_CONFIGURATIONS = Set.of("testFixturesImplementation");

    private static final Set<Set<String>> ALL_CONFIGURATIONS = Set.of(JAVA_CONFIGURATIONS,
                                                                      JAVA_LIBRARY_CONFIGURATIONS,
                                                                      JAVA_TEST_FIXTURES_CONFIGURATIONS);

    @Override
    public void apply(Project project) {
        applyCommonPlatformConfiguration(project);
    }

    private void applyCommonPlatformConfiguration(Project project) {
        project.getPlugins().withType(JavaPlugin.class, unused -> {
            var commonPlatform = project.getConfigurations().create(COMMON_PLATFORM_CONFIGURATION);

            ALL_CONFIGURATIONS.forEach(configurationSet -> extendConfiguration(project, commonPlatform, configurationSet));
        });
    }

    private void extendConfiguration(Project project, Configuration extendsFrom, Set<String> configurationSet) {
        configurationSet.stream()
                        .filter(configurationName -> hasConfiguration(project, configurationName))
                        .map(project.getConfigurations()::getByName)
                        .forEach(configuration -> configuration.extendsFrom(extendsFrom));
    }

    private boolean hasConfiguration(Project project, String configurationName) {
        return project.getConfigurations()
                      .stream()
                      .anyMatch(configuration -> configurationName.equals(configuration.getName()));
    }
}
