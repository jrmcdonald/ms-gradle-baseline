package com.jrmcdonald.common.baseline.manager.plugin;

import com.github.spotbugs.snom.SpotBugsPlugin;
import com.github.spotbugs.snom.SpotBugsReport;
import com.github.spotbugs.snom.SpotBugsTask;
import com.github.spotbugs.snom.internal.SpotBugsHtmlReport;
import com.github.spotbugs.snom.internal.SpotBugsXmlReport;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpotBugsPluginManagerTest {

    private SpotBugsPluginManager manager;
    private Project project;

    @BeforeEach
    void beforeEach() {
        manager = new SpotBugsPluginManager();

        project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
    }

    @Test
    @DisplayName("Should apply plugins to java projects")
    void shouldApplyPluginsToJavaProjects() {
        manager.apply(project);

        assertThat(project.getPlugins().hasPlugin(SpotBugsPlugin.class)).isTrue();
    }

    @Test
    @DisplayName("Should not apply expected plugins to non java projects")
    void shouldNotApplyPluginsToNonJavaProjects() {
        var nonJavaProject = ProjectBuilder.builder().build();

        manager.apply(nonJavaProject);

        assertThat(nonJavaProject.getPlugins().hasPlugin(SpotBugsPlugin.class)).isFalse();
    }

    @Test
    @DisplayName("Should enable html reports")
    void shouldEnableHtmlReports() {
        manager.apply(project);

        project.getTasks()
               .withType(SpotBugsTask.class, task -> assertThat(task.getReports().withType(SpotBugsHtmlReport.class)).isNotEmpty()
                                                                                                                     .allMatch(SpotBugsReport::isEnabled));
    }

    @Test
    @DisplayName("Should disable xml reports")
    void shouldDisableXmlReports() {
        manager.apply(project);

        project.getTasks()
               .withType(SpotBugsTask.class, task -> assertThat(task.getReports().withType(SpotBugsXmlReport.class)).isNotEmpty()
                                                                                                                    .noneMatch(SpotBugsReport::isEnabled));
    }

}