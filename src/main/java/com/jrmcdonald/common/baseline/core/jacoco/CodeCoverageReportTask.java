package com.jrmcdonald.common.baseline.core.jacoco;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;

import java.io.File;
import java.util.Objects;

@SuppressWarnings("java:S110")
public class CodeCoverageReportTask extends JacocoReport {

    public static final String NAME = "codeCoverageReport";

    public CodeCoverageReportTask() {
        setGroup("Verification");
        setDescription("Generates an aggregate code coverage report for all subproject test tasks.");

        init(getProject());
    }

    private void init(Project project) {
        project.getSubprojects().forEach(this::configureSubProjectsWithJacocoPlugin);
    }

    private void configureSubProjectsWithJacocoPlugin(Project project) {
        project.getPlugins().withType(JacocoPlugin.class).configureEach(unused -> configureSourceSetsAndExecutionData(project));
    }

    private void configureSourceSetsAndExecutionData(Project project) {
        project.getTasks().matching(JacocoUtils::hasJacocoTaskExtension).configureEach(task -> {
            var javaPluginConvention = project.getConvention().getPlugin(JavaPluginConvention.class);
            var main = Objects.requireNonNull(javaPluginConvention.getSourceSets().findByName("main"));

            var sourceExists = main.getAllJava().getSourceDirectories().getFiles().stream().allMatch(File::exists);

            // this accounts for projects with intermediate, empty, sub-projects
            if (sourceExists) {
                sourceSets(main);
                executionData(task);
            }
        });
    }

}
