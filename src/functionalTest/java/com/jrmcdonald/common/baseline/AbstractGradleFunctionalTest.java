package com.jrmcdonald.common.baseline;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.SneakyThrows;

public abstract class AbstractGradleFunctionalTest {

    private static final String SETTINGS_TEMPLATE_FILE = "src/functionalTest/resources/settings.gradle";
    private static final String GRADLE_TEMPLATE_FILE = "src/functionalTest/resources/build.gradle";
    protected static final String BUILD_SUCCESSFUL = "BUILD SUCCESSFUL";

    @TempDir
    protected File projectDir;

    @BeforeEach
    void configureProject() {
        copyToProject(SETTINGS_TEMPLATE_FILE, "settings.gradle");
        copyToProject(GRADLE_TEMPLATE_FILE, "build.gradle");
        configureGitHooksDirectory();
        configureGitRepository();
    }

    @SneakyThrows
    protected void configureGitHooksDirectory() {
        Files.createDirectory(new File(projectDir, ".githooks").toPath());
    }

    @SneakyThrows
    protected void configureGitRepository() {
        try (Repository gitRepository = FileRepositoryBuilder.create(new File(projectDir, ".git"))) {
            gitRepository.create();
        }
    }

    protected BuildResult build(String... arguments) {
        return GradleRunner.create()
                           .withPluginClasspath()
                           .withProjectDir(projectDir)
                           .withArguments(arguments)
                           .forwardOutput()
                           .build();
    }

    protected BuildResult buildAndFail(String... arguments) {
        return GradleRunner.create()
                           .withPluginClasspath()
                           .withProjectDir(projectDir)
                           .withArguments(arguments)
                           .forwardOutput()
                           .buildAndFail();
    }

    @SneakyThrows
    private void copyToProject(String source, String destination) {
        Files.write(new File(projectDir, destination).toPath(), Files.readAllBytes(Paths.get(source)));
    }

}
