package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.core.jacoco.CodeCoverageReportTask;
import com.jrmcdonald.common.baseline.core.jacoco.JacocoUtils;

import org.gradle.api.Project;
import org.gradle.testing.jacoco.plugins.JacocoPlugin;

import java.util.List;

import static com.jrmcdonald.common.baseline.core.constants.TaskNames.CHECK;
import static com.jrmcdonald.common.baseline.core.constants.TaskNames.TEST;
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
            configureCodeCoverageTask(project);
        } else {
            setCodeCoverageReportToDependOnTest(project);
        }
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(JacocoPlugin.class));
    }

    public void registerCodeCoverageReportTask(Project project) {
        project.getTasks().register(CodeCoverageReportTask.NAME, CodeCoverageReportTask.class);
    }

    private void configureCodeCoverageTask(Project project) {
        project.getTasks().withType(CodeCoverageReportTask.class, task -> {
            configureTaskOrdering(project, task);
            configureTaskReports(task);
        });
    }

    private void configureTaskOrdering(Project project, CodeCoverageReportTask task) {
        setCodeCoverageReportFinalizedByTest(project, task);
        setCheckDependsOnCodeCoverageReport(project, task);
    }

    private void configureTaskReports(CodeCoverageReportTask task) {
        task.getReports().getHtml().setEnabled(true);
        task.getReports().getXml().setEnabled(true);
    }

    private void setCodeCoverageReportFinalizedByTest(Project project, CodeCoverageReportTask task) {
        var testTask = project.getTasks().findByPath(getRootProjectPath(TEST));
        if (testTask != null) {
            testTask.setFinalizedBy(List.of(task));
        }
    }

    private void setCheckDependsOnCodeCoverageReport(Project project, CodeCoverageReportTask task) {
        var checkTask = project.getTasks().findByPath(getRootProjectPath(CHECK));
        if (checkTask != null) {
            checkTask.getDependsOn().add(task);
        }
    }

    private void setCodeCoverageReportToDependOnTest(Project project) {
        project.getTasks()
               .matching(JacocoUtils::hasJacocoTaskExtension)
               .forEach(task -> project.getRootProject()
                                       .getTasks()
                                       .withType(CodeCoverageReportTask.class, codeCoverageReportTask -> codeCoverageReportTask.setDependsOn(List.of(task))));
    }

}
