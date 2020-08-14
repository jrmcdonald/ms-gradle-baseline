package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JavaCompileConfigManagerTest {

    private Project rootProject;
    private Project subProject;

    @DisplayName("Java Project Tests")
    @Nested
    class JavaProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new JavaCompileConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();
            rootProject.getPlugins().apply(JavaPlugin.class);

            subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
            subProject.getPlugins().apply(JavaPlugin.class);

            List.of(rootProject, subProject).forEach(manager::apply);
        }

        @ParameterizedTest
        @ValueSource(strings = {"-Werror", "-Xlint:all", "-Xlint:try", "-Xlint:-processing"})
        @DisplayName("Should configure root project `javaCompile` compilerArgs")
        void shouldConfigureRootProjectJavaCompileCompilerArgs(String expectedArg) {
            assertThatCompilerArgsContains(rootProject, expectedArg);
        }

        @ParameterizedTest
        @ValueSource(strings = {"-Werror", "-Xlint:all", "-Xlint:try", "-Xlint:-processing"})
        @DisplayName("Should configure sub project `javaCompile` compilerArgs")
        void shouldConfigureSubProjectJavaCompileCompilerArgs(String expectedArg) {
            assertThatCompilerArgsContains(subProject, expectedArg);
        }

        private void assertThatCompilerArgsContains(Project project, String expectedArg) {
            project.getTasks().withType(JavaCompile.class, task -> assertThat(task.getOptions().getCompilerArgs()).contains(expectedArg));
        }

    }

    @DisplayName("Non Java Project Tests")
    @Nested
    class NonJavaProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new JavaCompileConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();

            manager.apply(rootProject);
        }

        @Test
        @DisplayName("Should not configure `javaCompile` compilerArgs")
        void shouldNotConfigureJavaCompileCompilerArgs() {
            rootProject.getTasks().withType(JavaCompile.class, task -> assertThat(task.getOptions().getCompilerArgs()).isEmpty());
        }

    }

}