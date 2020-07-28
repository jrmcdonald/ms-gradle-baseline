package com.jrmcdonald.common.baseline.manager;

import com.github.benmanes.gradle.versions.VersionsPlugin;
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask;
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentFilter;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.CHECK;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getSubProjectPath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VersionsPluginManagerTest extends AbstractPluginManagerTest {

    @Mock
    private ComponentFilter filter;

    private Project rootProject;
    private Project subProject;

    @BeforeEach
    void beforeEach() {
        var manager = new VersionsPluginManager();
        manager.setFilter(filter);

        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        rootProject.getPlugins().apply("java");
        rootProject.getRepositories().add(rootProject.getRepositories().mavenCentral());
        rootProject.getDependencies().add("implementation" , "org.eclipse.jgit:org.eclipse.jgit:1.0.0");

        subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
        subProject.getPlugins().apply("java");

        List.of(rootProject, subProject).forEach(manager::apply);
        List.of(rootProject, subProject).forEach(manager::afterEvaluate);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(filter);
    }

    @DisplayName("Apply Tests")
    @Nested
    class ApplyTests {

        @DisplayName("All Project Tests")
        @Nested
        class AllProjectTests {

            @Test
            @DisplayName("Should apply plugins to root project")
            void shouldApplyPluginsToRootProject() {
                assertThat(rootProject.getPlugins().hasPlugin(VersionsPlugin.class)).isTrue();
            }

            @Test
            @DisplayName("Should apply plugins to sub project")
            void shouldApplyPluginsToSubProject() {
                assertThat(subProject.getPlugins().hasPlugin(VersionsPlugin.class)).isTrue();
            }
        }


        @DisplayName("Root Project Tests")
        @Nested
        class RootProjectTests {

            @Test
            @DisplayName("Should configure checkConstraints")
            void shouldConfigureCheckConstraints() {
                rootProject.getTasks().withType(DependencyUpdatesTask.class, task -> assertThat(task.getCheckConstraints()).isTrue());
            }

            @Test
            @DisplayName("Should configure DependencyUpdateResolutionStrategy")
            void shouldConfigureDependencyUpdateResolutionStrategy() {
                when(filter.reject(any())).thenReturn(false);
                rootProject.getTasks().withType(DependencyUpdatesTask.class, DependencyUpdatesTask::dependencyUpdates);
                verify(filter).reject(any());
            }
        }

        @DisplayName("Sub Project Tests")
        @Nested
        class SubProjectTests {

            @Test
            @DisplayName("Should not configure checkConstraints")
            void shouldNotConfigureCheckConstraints() {
                subProject.getTasks().withType(DependencyUpdatesTask.class, task -> assertThat(task.getCheckConstraints()).isFalse());
            }

            @Test
            @DisplayName("Should not configure DependencyUpdateResolutionStrategy")
            void shouldNotConfigureDependencyUpdateResolutionStrategy() {
                subProject.getTasks().withType(DependencyUpdatesTask.class, DependencyUpdatesTask::dependencyUpdates);
                verifyNoInteractions(filter);
            }
        }
    }

    @DisplayName("After Evaluate Tests")
    @Nested
    class AfterEvaluateTests {

        @DisplayName("Root Project Tests")
        @Nested
        class RootProjectTests {

            @Test
            @DisplayName("Should set the `check` task to depend on the `dependencyUpdates` task")
            void shouldSetTheCheckTaskToDependOnTheDependencyUpdatesTask() {
                var dependsOn = findTaskByPath(rootProject, CHECK).getDependsOn();
                assertThat(dependsOn).hasAtLeastOneElementOfType(DependencyUpdatesTask.class);
            }
        }

        @DisplayName("Sub Project Tests")
        @Nested
        class SubProjectTests {

            @Test
            @DisplayName("Should not set the `check` task to depend on the `dependencyUpdates` task")
            void shouldNotSetTheCheckTaskToDependOnTheDependencyUpdatesTask() {
                var dependsOn = findTaskByPath(subProject, getSubProjectPath(subProject, CHECK)).getDependsOn();
                assertThat(dependsOn).doesNotHaveAnyElementsOfTypes(DependencyUpdatesTask.class);
            }
        }
    }

}