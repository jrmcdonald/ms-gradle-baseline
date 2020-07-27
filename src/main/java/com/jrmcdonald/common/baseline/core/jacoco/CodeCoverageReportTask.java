package com.jrmcdonald.common.baseline.core.jacoco;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;

@SuppressWarnings("java:S110")
public class CodeCoverageReportTask extends JacocoReport {

    public static final String NAME = "codeCoverageReport";

    public CodeCoverageReportTask() {
        init(getProject());
    }

    private void init(Project project) {
        getReports().getHtml().setEnabled(true);
        getReports().getXml().setEnabled(true);

        project.getSubprojects().forEach(this::configureSubProjectsWithJacocoPlugin);
    }

    private void configureSubProjectsWithJacocoPlugin(Project project) {
        project.getPlugins().withType(JacocoPlugin.class).configureEach(unused -> configureSourceSetsAndExecutionData(project));
    }

    private void configureSourceSetsAndExecutionData(Project project) {
        project.getTasks().matching(JacocoUtils::hasJacocoTaskExtension).configureEach(task -> {
            var javaPluginConvention = project.getConvention().getPlugin(JavaPluginConvention.class);
            var main = javaPluginConvention.getSourceSets().findByName("main");

            sourceSets(main);
            executionData(task);
        });
    }

}
