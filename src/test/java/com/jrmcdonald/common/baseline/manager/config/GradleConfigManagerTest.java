package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.artifacts.UnknownConfigurationException;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaTestFixturesPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Objects;

import static com.jrmcdonald.common.baseline.manager.config.GradleConfigManager.COMMON_PLATFORM_CONFIGURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GradleConfigManagerTest {

    private Project rootProject;
    private Project subProject;

    @DisplayName("Java Project Tests")
    @Nested
    class JavaProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new GradleConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();
            rootProject.getPlugins().apply(JavaPlugin.class);

            subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
            subProject.getPlugins().apply(JavaPlugin.class);

            List.of(rootProject, subProject).forEach(manager::apply);
        }

        @Test
        @DisplayName("Should define `commonPlatform` configuration")
        void shouldDefineCommonPlatformConfiguration() {
            assertThat(rootProject.getConfigurations().getByName(COMMON_PLATFORM_CONFIGURATION)).isNotNull();
        }

        @ParameterizedTest
        @ValueSource(strings = {"compileOnly", "annotationProcessor", "testCompileOnly", "testAnnotationProcessor", "implementation"})
        @DisplayName("Should extend default configurations from `commonPlatform`")
        void shouldExtendDefaultConfigurationsFromCommonPlatform(String configurationName) {
            validateConfigurationExtendsFromParentConfiguration(rootProject, configurationName, COMMON_PLATFORM_CONFIGURATION);
            validateConfigurationExtendsFromParentConfiguration(subProject, configurationName, COMMON_PLATFORM_CONFIGURATION);
        }

    }

    @DisplayName("Java Library Project Tests")
    @Nested
    class JavaLibraryProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new GradleConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();
            rootProject.getPlugins().apply(JavaLibraryPlugin.class);

            subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
            subProject.getPlugins().apply(JavaLibraryPlugin.class);

            List.of(rootProject, subProject).forEach(manager::apply);
        }

        @ParameterizedTest
        @ValueSource(strings = "api")
        @DisplayName("Should extend default configurations from `commonPlatform`")
        void shouldExtendDefaultConfigurationsFromCommonPlatform(String configurationName) {
            validateConfigurationExtendsFromParentConfiguration(rootProject, configurationName, COMMON_PLATFORM_CONFIGURATION);
            validateConfigurationExtendsFromParentConfiguration(subProject, configurationName, COMMON_PLATFORM_CONFIGURATION);
        }

    }

    @DisplayName("Java Text Fixtures Project Tests")
    @Nested
    class JavaTestFixturesProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new GradleConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();
            rootProject.getPlugins().apply(JavaPlugin.class);
            rootProject.getPlugins().apply(JavaTestFixturesPlugin.class);

            subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
            subProject.getPlugins().apply(JavaPlugin.class);
            subProject.getPlugins().apply(JavaTestFixturesPlugin.class);

            List.of(rootProject, subProject).forEach(manager::apply);
        }

        @ParameterizedTest
        @ValueSource(strings = "testFixturesImplementation")
        @DisplayName("Should extend default configurations from `commonPlatform`")
        void shouldExtendDefaultConfigurationsFromCommonPlatform(String configurationName) {
            validateConfigurationExtendsFromParentConfiguration(rootProject, configurationName, COMMON_PLATFORM_CONFIGURATION);
            validateConfigurationExtendsFromParentConfiguration(subProject, configurationName, COMMON_PLATFORM_CONFIGURATION);
        }

    }

    @DisplayName("Non Java Project Tests")
    @Nested
    class NonJavaProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new GradleConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();

            manager.apply(rootProject);
        }

        @Test
        @DisplayName("Should not define `commonPlatform` configuration")
        void shouldNotDefineCommonPlatformConfiguration() {
            var configurations = rootProject.getConfigurations();
            assertThatThrownBy(() -> configurations.getByName(COMMON_PLATFORM_CONFIGURATION)).isInstanceOf(UnknownConfigurationException.class)
                                                                                .hasMessage("Configuration with name 'commonPlatform' not found.");
        }

    }

    private void validateConfigurationExtendsFromParentConfiguration(Project project, String configurationName, String parentConfigurationName) {
        var parentConfiguration = Objects.requireNonNull(project.getConfigurations().getByName(parentConfigurationName));
        var configuration = Objects.requireNonNull(project.getConfigurations().getByName(configurationName));

        assertThat(configuration.getExtendsFrom()).contains(parentConfiguration);
    }
}