package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.core.jacoco.CodeCoverageReportTask;
import com.jrmcdonald.common.baseline.core.jacoco.JacocoUtils;

import org.gradle.api.Action;
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
        configureRootProject(project);
        configureSubProject(project);
    }

    private void applyToJavaProject(Project project) {
        project.getPluginManager().withPlugin("java", unused -> project.getPluginManager().apply(JacocoPlugin.class));
    }

    private void configureRootProject(Project project) {
        if (isRootProject(project)) {
            project.getTasks().register(CodeCoverageReportTask.NAME, CodeCoverageReportTask.class);
            project.getTasks().withType(CodeCoverageReportTask.class, configureTaskOrdering(project));
        }
    }

    private void configureSubProject(Project project) {
        if (!isRootProject(project)) {
            setCodeCoverageReportToDependOnTest(project);
        }
    }

    private Action<CodeCoverageReportTask> configureTaskOrdering(Project project) {
        return task -> {
            setCodeCoverageReportFinalizedByTest(project, task);
            setCheckDependsOnCodeCoverageReport(project, task);
        };
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
