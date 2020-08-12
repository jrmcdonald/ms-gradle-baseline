package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.core.jacoco.CodeCoverageReportTask;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sonarqube.gradle.SonarQubePlugin;
import org.sonarqube.gradle.SonarQubeTask;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

class SonarQubePluginManagerTest extends AbstractPluginManagerTest {

    private static final String CODE_COVERAGE_REPORT_PATH = "%s/reports/jacoco/codeCoverageReport/codeCoverageReport.xml";
    private static final String JACOCO_TEST_REPORT_PATH = "%s/reports/jacoco/test/jacocoTestReport.xml";

    SonarQubePluginManager manager;
    Project rootProject;
    Project subProject;

    @BeforeEach
    void beforeEach() {
        manager = new SonarQubePluginManager();

        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        rootProject.getTasks().register(CodeCoverageReportTask.NAME, CodeCoverageReportTask.class);

        subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();

        List.of(rootProject, subProject).forEach(manager::apply);
        List.of(rootProject, subProject).forEach(manager::afterEvaluate);
    }

    @DisplayName("Apply Tests")
    @Nested
    class ApplyTests {

        @DisplayName("All Project Test")
        @Nested
        class AllProjectTest {

            @Test
            @DisplayName("Should apply plugins to root project")
            void shouldApplyPluginsToRootProject() {
                assertThat(rootProject.getPlugins().hasPlugin(SonarQubePlugin.class)).isTrue();
            }

            @Test
            @DisplayName("Should not apply plugins to non root project")
            void shouldNotApplyPluginsToNonRootProject() {
                assertThat(subProject.getPlugins().hasPlugin(SonarQubePlugin.class)).isFalse();
            }
        }

        @DisplayName("Root Project Tests")
        @TestInstance(PER_CLASS)
        @Nested
        class RootProjectTests {

            @ParameterizedTest(name = "[{index}] set `{0}` to `{1}`")
            @MethodSource("defaultProperties")
            @DisplayName("Should configure the `SonarQubeExtension` with default properties")
            void shouldConfigureTheSonarQubeExtensionWithDefaultProperties(String key, String value) {
                rootProject.getTasks().withType(SonarQubeTask.class, task -> assertThat(task.getProperties()).containsEntry(key, value));
            }

            private Stream<Arguments> defaultProperties() {
                return Stream.of(
                        Arguments.of("sonar.projectKey", "jrmcdonald_rootProject"),
                        Arguments.of("sonar.organization", "jrmcdonald"),
                        Arguments.of("sonar.host.url", "https://sonarcloud.io")
                );
            }

            @Test
            @DisplayName("Should configure the `xmlReportPaths` property")
            void shouldConfigureTheXmlReportPathsProperty() {
                var expectedCodeCoverageReportPath = format(CODE_COVERAGE_REPORT_PATH, rootProject.getBuildDir().toPath());
                var expectedJacocoTestReportPath = format(JACOCO_TEST_REPORT_PATH, rootProject.getBuildDir().toPath());

                rootProject.getTasks()
                           .withType(SonarQubeTask.class,
                                     task -> assertThat(task.getProperties()).containsEntry("sonar.coverage.jacoco.xmlReportPaths",
                                                                                                                 join(",", expectedCodeCoverageReportPath, expectedJacocoTestReportPath)));
            }
        }

        @DisplayName("Sub Project Tests")
        @TestInstance(PER_CLASS)
        @Nested
        class SubProjectTests {

            @ParameterizedTest(name = "[{index}] should not set `{0}`")
            @MethodSource("defaultPropertyKeys")
            @DisplayName("Should not configure the `SonarQubeExtension` with default properties")
            void shouldNotConfigureTheSonarQubeExtensionWithDefaultProperties(String key) {
                subProject.getTasks().withType(SonarQubeTask.class, task -> assertThat(task.getProperties()).doesNotContainKey(key));
            }

            private Stream<Arguments> defaultPropertyKeys() {
                return Stream.of(
                        Arguments.of("sonar.projectKey"),
                        Arguments.of("sonar.organization"),
                        Arguments.of("sonar.host.url"),
                        Arguments.of("sonar.coverage.jacoco.xmlReportPaths")
                );
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
            @DisplayName("Should set the `sonarqube` task to must run after the `codeCoverageReport` task")
            void shouldSetTheSonarqubeTaskToMustRunAfterTheCodeCoverageReportTask() {
                var mustRunAfter = findTaskByPath(rootProject, "sonarqube").getMustRunAfter().getDependencies(null);
                assertThat(mustRunAfter).hasAtLeastOneElementOfType(CodeCoverageReportTask.class);
            }
        }
    }
}