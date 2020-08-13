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
import java.nio.file.StandardOpenOption;

import javax.annotation.Nonnull;

import lombok.SneakyThrows;

import static java.lang.String.format;

public abstract class AbstractGradleFunctionalTest {

    // File Names
    protected static final String BUILD_GRADLE = "build.gradle";
    private static final String SETTINGS_GRADLE = "settings.gradle";

    // Directories
    private static final String SOURCE_RESOURCES_DIR = "src/functionalTest/resources";
    private static final String SOURCE_SNIPPETS_DIR = format("%s/snippets", SOURCE_RESOURCES_DIR);
    private static final String SOURCE_CODE_DIR = format("%s/code", SOURCE_RESOURCES_DIR);

    // Files
    private static final String SETTINGS_TEMPLATE_FILE = format("%s/%s", SOURCE_RESOURCES_DIR, SETTINGS_GRADLE);
    private static final String GRADLE_TEMPLATE_FILE = format("%s/%s", SOURCE_RESOURCES_DIR, BUILD_GRADLE);
    protected static final String DEPENDENCIES_FILE = format("%s/dependencies.gradle", SOURCE_SNIPPETS_DIR);
    protected static final String APPLICATION_FILE = format("%s/Application.java", SOURCE_CODE_DIR);
    protected static final String APPLICATION_TEST_FILE = format("%s/ApplicationTest.java", SOURCE_CODE_DIR);

    protected static final String BUILD_SUCCESSFUL = "BUILD SUCCESSFUL";

    @TempDir
    protected File projectDir;

    @BeforeEach
    void configureProject() {
        copyToProject(SETTINGS_TEMPLATE_FILE, SETTINGS_GRADLE);
        copyToProject(GRADLE_TEMPLATE_FILE, BUILD_GRADLE);
        configureGitHooksDirectory();
        configureGitRepository();
    }

    @SneakyThrows
    protected void configureGitHooksDirectory() {
        Files.createDirectory(new File(projectDir, ".githooks").toPath());
    }

    @SneakyThrows
    protected void configureGitRepository() {
        try (Repository gitRepository = createRepository()) {
            gitRepository.create();
        }
    }

    /**
     * Workaround for https://github.com/spotbugs/spotbugs/issues/756
     *
     * @return the created git repository
     */
    @SneakyThrows
    @Nonnull
    private Repository createRepository() {
        return FileRepositoryBuilder.create(new File(projectDir, ".git"));
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
    protected void copyToProject(@Nonnull String source, @Nonnull String destination) {
        var destinationPath = new File(projectDir, destination).toPath();

        Files.createDirectories(destinationPath.getParent());
        Files.write(destinationPath, Files.readAllBytes(Paths.get(source)));
    }

    @SneakyThrows
    protected void appendToProject(@Nonnull String source, String destination) {
        Files.write(new File(projectDir, destination).toPath(), Files.readAllBytes(Paths.get(source)), StandardOpenOption.APPEND);
    }
}
