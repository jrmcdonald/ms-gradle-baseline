package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.core.jacoco.CodeCoverageReportTask;

import org.gradle.api.Project;
import org.sonarqube.gradle.SonarQubeExtension;
import org.sonarqube.gradle.SonarQubePlugin;
import org.sonarqube.gradle.SonarQubeTask;

import java.util.List;

import static com.jrmcdonald.common.baseline.util.ProjectUtils.isRootProject;
import static com.jrmcdonald.common.baseline.util.TaskPathUtils.getRootProjectPath;
import static java.lang.String.format;

public class SonarQubePluginManager implements PluginManager {

    private static final String SONAR_PROJECT_KEY = "sonar.projectKey";
    private static final String SONAR_ORGANIZATION_KEY = "sonar.organization";
    private static final String SONAR_HOST_URL_KEY = "sonar.host.url";
    private static final String SONAR_COVERAGE_PATH_KEY = "sonar.coverage.jacoco.xmlReportPaths";

    private static final String SONAR_PROJECT_KEY_FORMAT = "jrmcdonald_%s";
    private static final String SONAR_ORGANISATION_VALUE = "jrmcdonald";
    private static final String SONAR_HOST_URL_VALUE = "https://sonarcloud.io";
    private static final String SONAR_COVERAGE_PATH_FORMAT = "%s/reports/jacoco/codeCoverageReport/codeCoverageReport.xml";

    @Override
    public void apply(Project project) {
        applyToRootProject(project);
        configureExtension(project);
    }

    @Override
    public void afterEvaluate(Project project) {
        configureTaskOrdering(project);
    }

    public void applyToRootProject(Project project) {
        if (isRootProject(project)) {
            project.getPlugins().apply(SonarQubePlugin.class);
        }
    }

    private void configureExtension(Project project) {
        if (isRootProject(project)) {
            var extension = project.getExtensions().findByType(SonarQubeExtension.class);
            if (extension != null) {
                extension.properties(properties -> {
                    properties.property(SONAR_PROJECT_KEY, format(SONAR_PROJECT_KEY_FORMAT, project.getName()));
                    properties.property(SONAR_ORGANIZATION_KEY, SONAR_ORGANISATION_VALUE);
                    properties.property(SONAR_HOST_URL_KEY, SONAR_HOST_URL_VALUE);
                    properties.property(SONAR_COVERAGE_PATH_KEY, format(SONAR_COVERAGE_PATH_FORMAT, project.getBuildDir().toPath()));
                });
            }
        }
    }

    private void configureTaskOrdering(Project project) {
        if (isRootProject(project)) {
            project.getTasks().withType(SonarQubeTask.class, sonarQubeTask -> {
                var codeCoverageReportTask = project.getTasks().findByPath(getRootProjectPath(CodeCoverageReportTask.NAME));
                if (codeCoverageReportTask != null) {
                    sonarQubeTask.setMustRunAfter(List.of(codeCoverageReportTask));
                }
            });
        }
    }
}
