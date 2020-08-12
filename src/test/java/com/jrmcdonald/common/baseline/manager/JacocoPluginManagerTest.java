package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.core.jacoco.CodeCoverageReportTask;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.CHECK;
import static com.jrmcdonald.common.baseline.core.constants.TaskNames.JACOCO_TEST_REPORT;
import static com.jrmcdonald.common.baseline.core.constants.TaskNames.TEST;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getRootProjectPath;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getSubProjectPath;
import static org.assertj.core.api.Assertions.assertThat;

class JacocoPluginManagerTest extends AbstractPluginManagerTest {

    private JacocoPluginManager manager;
    private Project rootProject;
    private Project subProject;

    @BeforeEach
    void beforeEach() {
        manager = new JacocoPluginManager();

        rootProject = ProjectBuilder.builder().withName("rootProject").build();
        rootProject.getPlugins().apply("java");

        subProject = ProjectBuilder.builder().withName("subProject").withParent(rootProject).build();
        subProject.getPlugins().apply("java");

        List.of(rootProject, subProject).forEach(manager::apply);
        List.of(rootProject, subProject).forEach(manager::afterEvaluate);
    }

    @DisplayName("Apply Tests")
    @Nested
    class ApplyTests {

        @DisplayName("All Java Projects Tests")
        @Nested
        class AllJavaProjectsTests {

            @Test
            @DisplayName("Should apply plugins to root projects")
            void shouldApplyPluginsToRootProjects() {
                assertThat(rootProject.getPlugins().hasPlugin(JacocoPlugin.class)).isTrue();
            }

            @Test
            @DisplayName("Should apply plugins to sub projects")
            void shouldApplyPluginsToSubProjects() {
                assertThat(subProject.getPlugins().hasPlugin(JacocoPlugin.class)).isTrue();
            }
        }

        @DisplayName("Root Project Tests")
        @Nested
        class RootProjectTests {

            @Test
            @DisplayName("Should configure the `codeCoverageReport` task")
            void shouldConfigureTheCodeCoverageReportTask() {
                var task = rootProject.getTasks().findByPath(getRootProjectPath(CodeCoverageReportTask.NAME));
                assertThat(task).isInstanceOf(CodeCoverageReportTask.class);
            }
        }

        @DisplayName("Sub Project Tests")
        @Nested
        class SubProjectTests {

            @Test
            @DisplayName("Should not configure the `codeCoverageReport` task")
            void shouldNotConfigureTheCodeCoverageReportTask() {
                assertThat(subProject.getTasks().findByPath(getSubProjectPath(subProject, CodeCoverageReportTask.NAME))).isNull();
            }
        }

        @Test
        @DisplayName("Should not apply expected plugins to non java projects")
        void shouldNotApplyPluginsToNonJavaProjects() {
            var nonJavaProject = ProjectBuilder.builder().build();

            manager.apply(nonJavaProject);

            assertThat(nonJavaProject.getPlugins().hasPlugin(JacocoPlugin.class)).isFalse();
        }
    }

    @DisplayName("After Evaluate Tests")
    @Nested
    class AfterEvaluateTests {

        @DisplayName("Root Project Tests")
        @Nested
        class RootProjectTests {

            @DisplayName("CodeCoverageReport Tests")
            @Nested
            class CodeCoverageReportTests {

                @Test
                @DisplayName("Should set the `test` task to be finalized by the `codeCoverageReport` task")
                void shouldSetTheTestTaskToBeFinalizedByTheCodeCoverageReportTask() {
                    var finalizedBy = findTaskByPath(rootProject, getRootProjectPath(TEST)).getFinalizedBy().getDependencies(null);
                    assertThat(finalizedBy).hasAtLeastOneElementOfType(CodeCoverageReportTask.class);
                }

                @Test
                @DisplayName("Should set the `check` task to depend on the `codeCoverageReport` task")
                void shouldSetTheCheckTaskToDependOnTheCodeCoverageReportTask() {
                    var dependsOn = findTaskByPath(rootProject, getRootProjectPath(CHECK)).getDependsOn();
                    assertThat(dependsOn).hasAtLeastOneElementOfType(CodeCoverageReportTask.class);
                }

                @Test
                @DisplayName("Should enable html reports")
                void shouldEnableHtmlReports() {
                    rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> assertThat(task.getReports().getHtml().isEnabled()).isTrue());
                }

                @Test
                @DisplayName("Should enable xml reports")
                void shouldEnableXmlReports() {
                    rootProject.getTasks().withType(CodeCoverageReportTask.class, task -> assertThat(task.getReports().getXml().isEnabled()).isTrue());
                }
            }

            @DisplayName("JacocoTestReport Tests")
            @Nested
            class JacocoTestReportTests {

                @Test
                @DisplayName("Should set the `test` task to be finalized by the `jacocoTestReport` task")
                void shouldSetTheTestTaskToBeFinalizedByTheJacocoTestReportTask() {
                    var finalizedBy = findTaskByPath(rootProject, getRootProjectPath(TEST)).getFinalizedBy().getDependencies(null);
                    assertThat(finalizedBy).extracting("identity.name").contains(JACOCO_TEST_REPORT);
                }

                @Test
                @DisplayName("Should set the `check` task to depend on the `jacocoTestReport` task")
                void shouldSetTheCheckTaskToDependOnTheJacocoTestReportTask() {
                    var dependsOn = findTaskByPath(rootProject, getRootProjectPath(CHECK)).getDependsOn();
                    assertThat(dependsOn).extracting("identity.name").contains(JACOCO_TEST_REPORT);
                }

                @Test
                @DisplayName("Should set the `jacocoTestReport` task to depend on the root project `test` task")
                void shouldSetTheJacocoTestReportTaskToDependOnTheRootProjectTestTask() {
                    var dependsOn = findTaskByPath(rootProject, getRootProjectPath(JACOCO_TEST_REPORT)).getDependsOn();
                    assertThat(dependsOn).contains(rootProject.getTasks().findByPath(getRootProjectPath(TEST)));
                }

                @Test
                @DisplayName("Should enable html reports")
                void shouldEnableHtmlReports() {
                    var task = (JacocoReport) findTaskByPath(rootProject, getRootProjectPath(JACOCO_TEST_REPORT));
                   assertThat(task.getReports().getHtml().isEnabled()).isTrue();
                }

                @Test
                @DisplayName("Should enable xml reports")
                void shouldEnableXmlReports() {
                    var task = (JacocoReport) findTaskByPath(rootProject, getRootProjectPath(JACOCO_TEST_REPORT));
                    assertThat(task.getReports().getXml().isEnabled()).isTrue();                }
            }
        }

        @DisplayName("Sub Project Tests")
        @Nested
        class SubProjectTests {

            @Test
            @DisplayName("Should set the `codeCoverageReport` task to depend on the sub project `test` task")
            void shouldSetTheCodeCoverageReportTaskToDependOnTheSubProjectTestTask() {
                var dependsOn = findTaskByPath(rootProject, getRootProjectPath(CodeCoverageReportTask.NAME)).getDependsOn();
                assertThat(dependsOn).contains(subProject.getTasks().findByPath(getSubProjectPath(subProject, TEST)));
            }

            @Test
            @DisplayName("Should not set the `test` task to be finalized by the `codeCoverageReport` task")
            void shouldNotSetTheTestTaskToBeFinalizedByTheCodeCoverageReportTask() {
                var finalizedBy = findTaskByPath(subProject, getSubProjectPath(subProject, TEST)).getFinalizedBy().getDependencies(null);
                assertThat(finalizedBy).doesNotHaveAnyElementsOfTypes(CodeCoverageReportTask.class);
            }

            @Test
            @DisplayName("Should not set the `check` task to depend on the `codeCoverageReport` task")
            void shouldNotSetTheCheckTaskToDependOnTheCodeCoverageReportTask() {
                var dependsOn = findTaskByPath(subProject, getSubProjectPath(subProject, CHECK)).getDependsOn();
                assertThat(dependsOn).doesNotHaveAnyElementsOfTypes(CodeCoverageReportTask.class);
            }

        }
    }

}