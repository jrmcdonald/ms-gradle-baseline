package com.jrmcdonald.common.baseline.manager.plugin;

import com.jrmcdonald.common.baseline.core.jacoco.CodeCoverageReportTask;
import com.jrmcdonald.common.baseline.core.jacoco.JacocoUtils;

import org.gradle.api.Project;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;
import org.gradle.testing.jacoco.tasks.JacocoReport;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.CHECK;
import static com.jrmcdonald.common.baseline.core.constants.TaskNames.JACOCO_TEST_REPORT;
import static com.jrmcdonald.common.baseline.core.constants.TaskNames.TEST;
import static com.jrmcdonald.common.baseline.util.ProjectUtils.findTaskByName;
import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getRootProjectPath;

public class JacocoPluginManager implements PluginManager {

    @Override
    public void apply(Project project) {
        applyToJavaProject(project);

        if (isRootProject(project)) {
            registerCodeCoverageReportTask(project);
        }
    }

    @Override
    public void afterEvaluate(Project project) {
        if (isRootProject(project)) {
            configureJacocoReportTasks(project);
            setJacocoReportToDependOnTest(project, JACOCO_TEST_REPORT);
        } else {
            setJacocoReportToDependOnTest(project, CodeCoverageReportTask.NAME);
        }
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(JacocoPlugin.class));
    }

    public void registerCodeCoverageReportTask(Project project) {
        project.getTasks().register(CodeCoverageReportTask.NAME, CodeCoverageReportTask.class);
    }

    private void configureJacocoReportTasks(Project project) {
        // configure both our custom report task and the standard jacocoTestReport task
        project.getTasks().withType(JacocoReport.class, task -> {
            configureTaskOrdering(project, task);
            configureTaskReports(task);
        });
    }

    private void configureTaskOrdering(Project project, JacocoReport task) {
        setTaskFinalizedByTest(project, task);
        setCheckDependsOnTask(project, task);
    }

    private void setTaskFinalizedByTest(Project project, JacocoReport task) {
        var testTask = project.getTasks().findByPath(getRootProjectPath(TEST));
        if (testTask != null) {
            testTask.finalizedBy(task);
        }
    }

    private void setCheckDependsOnTask(Project project, JacocoReport task) {
        var checkTask = project.getTasks().findByPath(getRootProjectPath(CHECK));
        if (checkTask != null) {
            checkTask.dependsOn(task);
        }
    }

    private void configureTaskReports(JacocoReport task) {
        task.getReports().getHtml().setEnabled(true);
        task.getReports().getXml().setEnabled(true);
    }

    private void setJacocoReportToDependOnTest(Project project, String jacocoReportTaskName) {
        project.getTasks()
               .matching(JacocoUtils::hasJacocoTaskExtension)
               .forEach(findTaskByName(project.getRootProject(), jacocoReportTaskName, JacocoReport.class)::dependsOn);
    }

}
