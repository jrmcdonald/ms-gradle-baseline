package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.plugin.BaselinePluginExtension;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;
import org.springframework.boot.gradle.tasks.bundling.BootJar;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.JAR;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC", justification = "junit nested requires non static")
class SpringBootPluginManagerTest {

    private SpringBootPluginManager manager;
    private Project project;

    @BeforeEach
    void beforeEach() {
        manager = new SpringBootPluginManager();

        project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
    }

    @DisplayName("After Evaluate Tests")
    @Nested
    class AfterEvaluateTests {

        @DisplayName("Spring Boot Plugin Disabled Tests")
        @Nested
        class SpringBootPluginDisabledTests {

            @Test
            @DisplayName("Should not apply plugins when disabled")
            void shouldNotApplyPluginsWhenDisabled() {
                var extension = project.getExtensions().create("baseline", BaselinePluginExtension.class);
                extension.setSpringBootEnabled(false);

                manager.afterEvaluate(project);

                assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class)).isFalse();
            }
        }

        @DisplayName("Java Project Tests")
        @Nested
        class JavaProjectTests {

            @BeforeEach
            void beforeEach() {
                project.getExtensions().create("baseline", BaselinePluginExtension.class);

                manager.afterEvaluate(project);

                // force afterEvaluate hook to be called
                project.getTasksByName("tasks", false);
            }

            @Test
            @DisplayName("Should apply plugins to java projects")
            void shouldApplyPluginsToJavaProjects() {
                assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class)).isTrue();
            }

            @DisplayName("With Spring Boot Plugin Applied Tests")
            @Nested
            class WithSpringBootPluginAppliedTests {

                @Test
                @DisplayName("Should disable the `bootJar` task by default")
                void shouldDisableTheBootJarTaskByDefault() {
                    project.getTasks().withType(BootJar.class, task -> assertThat(task.isEnabled()).isFalse());
                }

                @Test
                @DisplayName("Should re-enable the `jar` task by default")
                void shouldReEnableTheJarTaskByDefault() {
                    var jarTask = project.getTasks().findByName(JAR);
                    assertThat(jarTask).isNotNull();
                    assertThat(jarTask.getEnabled()).isTrue();
                }
            }

            @DisplayName("Without Spring Boot Plugin Applied Tests")
            @Nested
            class WithoutSpringBootPluginAppliedTests {

                // difficult to test this as bootJar task is null and jar task is enabled by default anyway

            }
        }

        @DisplayName("Non Java Project Tests")
        @Nested
        class NonJavaProjectTests {

            @Test
            @DisplayName("Should not apply expected plugins to non java projects")
            void shouldNotApplyPluginsToNonJavaProjects() {
                var nonJavaProject = ProjectBuilder.builder().build();
                nonJavaProject.getExtensions().create("baseline", BaselinePluginExtension.class);

                manager.apply(nonJavaProject);

                assertThat(nonJavaProject.getPlugins().hasPlugin(SpringBootPlugin.class)).isFalse();
            }
        }
    }
}