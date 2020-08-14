package com.jrmcdonald.common.baseline.manager.config;

import org.gradle.api.Project;
import org.gradle.api.internal.tasks.testing.junit.JUnitTestFramework;
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JunitConfigManagerTest {

    private static final Class<org.gradle.api.tasks.testing.Test> GRADLE_TEST_TASK_CLASS = org.gradle.api.tasks.testing.Test.class;

    private Project rootProject;
    private Project subProject;

    @DisplayName("Java Project Tests")
    @Nested
    class JavaProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new JunitConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();
            rootProject.getPlugins().apply(JavaPlugin.class);

            subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
            subProject.getPlugins().apply(JavaPlugin.class);

            List.of(rootProject, subProject).forEach(manager::apply);
        }

        @Test
        @DisplayName("Should configure root project `test` task with `useJunitPlatform`")
        void shouldConfigureRootProjectTestTaskWithUseJunitPlatform() {
            rootProject.getTasks().withType(GRADLE_TEST_TASK_CLASS, this::assertThatTestFrameworkIsJunitPlatform);
        }

        @Test
        @DisplayName("Should configure sub project `test` task with `useJunitPlatform`")
        void shouldConfigureSubProjectTestTaskWithUseJunitPlatform() {
            subProject.getTasks().withType(GRADLE_TEST_TASK_CLASS, this::assertThatTestFrameworkIsJunitPlatform);
        }

        private void assertThatTestFrameworkIsJunitPlatform(org.gradle.api.tasks.testing.Test task) {
            assertThat(task.getTestFramework()).isInstanceOf(JUnitPlatformTestFramework.class);
        }

    }

    @DisplayName("Non Java Project Tests")
    @Nested
    class NonJavaProjectTests {

        @BeforeEach
        void beforeEach() {
            var manager = new JunitConfigManager();

            rootProject = ProjectBuilder.builder().withName("rootProject").build();

            manager.apply(rootProject);
        }


        @Test
        @DisplayName("Should not configure `test` with `useJunitPlatform`")
        void shouldNotConfigureTestWithUseJunitPlatform() {
            rootProject.getTasks().withType(GRADLE_TEST_TASK_CLASS, task -> assertThat(task.getTestFramework()).isInstanceOf(JUnitTestFramework.class));
        }

    }

}