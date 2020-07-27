package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;
import org.owasp.dependencycheck.gradle.tasks.Aggregate;
import org.owasp.dependencycheck.reporting.ReportGenerator.Format;

import java.util.List;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.BUILD;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getRootProjectPath;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getSubProjectPath;
import static org.assertj.core.api.Assertions.assertThat;

class DependencyCheckPluginManagerTest extends AbstractPluginManagerTest {

    private Project rootProject;
    private Project subProject;

    @BeforeEach
    void beforeEach() {
        var manager = new DependencyCheckPluginManager();

        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        rootProject.getPlugins().apply("java");

        subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
        subProject.getPlugins().apply("java");

        List.of(rootProject, subProject).forEach(manager::apply);
    }

    @DisplayName("All Project Tests")
    @Nested
    class AllProjectTests {

        @Test
        @DisplayName("Should apply plugins to root project")
        void shouldApplyPluginsToRootProject() {
            assertThat(rootProject.getPlugins().hasPlugin(DependencyCheckPlugin.class)).isTrue();
        }

        @Test
        @DisplayName("Should apply plugins to sub project")
        void shouldApplyPluginsToSubProject() {
            assertThat(subProject.getPlugins().hasPlugin(DependencyCheckPlugin.class)).isTrue();
        }
    }

    @DisplayName("Root Project Tests")
    @Nested
    class RootProjectTests {

        @Test
        @DisplayName("Should set format to ALL")
        void shouldSetFormatToAll() {
            var format = findExtensionByType(rootProject, DependencyCheckExtension.class).getFormat();
            assertThat(format).isEqualTo(Format.ALL);
        }

        @Test
        @DisplayName("Should set nodeEnabled on analyzers to false")
        void shouldSetNodeEnabledOnAnalyzersToFalse() {
            var nodeEnabled = findExtensionByType(rootProject, DependencyCheckExtension.class).getAnalyzers().getNodeEnabled();
            assertThat(nodeEnabled).isFalse();
        }

        @Test
        @DisplayName("Should set the `build` task to depend on the `dependencyCheckAggregate` task")
        void shouldSetTheBuildTaskToDependOnTheDependencyCheckAggregateTask() {
            var dependsOn = findTaskByPath(rootProject, getRootProjectPath(BUILD)).getDependsOn();
            assertThat(dependsOn).hasAtLeastOneElementOfType(Aggregate.class);

        }
    }

    @DisplayName("Sub Project Tests")
    @Nested
    class SubProjectTests {

        @Test
        @DisplayName("Should not set format to ALL")
        void shouldNotSetFormatToAll() {
            var format = findExtensionByType(subProject, DependencyCheckExtension.class).getFormat();
            assertThat(format).isEqualTo(Format.HTML);
        }

        @Test
        @DisplayName("Should not set nodeEnabled on analyzers to false")
        void shouldNotSetNodeEnabledOnAnalyzersToFalse() {
            var nodeEnabled = findExtensionByType(subProject, DependencyCheckExtension.class).getAnalyzers().getNodeEnabled();
            assertThat(nodeEnabled).isNull();
        }

        @Test
        @DisplayName("Should not set the `build` task to depend on the `dependencyCheckAggregate` task")
        void shouldNotSetTheBuildTaskToDependOnTheDependencyCheckAggregateTask() {
            var dependsOn = findTaskByPath(subProject, getSubProjectPath(subProject, BUILD)).getDependsOn();
            assertThat(dependsOn).doesNotHaveAnyElementsOfTypes(Aggregate.class);
        }
    }

}