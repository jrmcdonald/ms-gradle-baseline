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

public abstract class AbstractGradleFunctionalTest {

    protected static final String SETTINGS_GRADLE = "settings.gradle";
    protected static final String BUILD_GRADLE = "build.gradle";
    private static final String SETTINGS_TEMPLATE_FILE = "src/functionalTest/resources/settings.gradle";
    private static final String GRADLE_TEMPLATE_FILE = "src/functionalTest/resources/build.gradle";
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
    private void copyToProject(@Nonnull String source, @Nonnull String destination) {
        Files.write(new File(projectDir, destination).toPath(), Files.readAllBytes(Paths.get(source)));
    }

    @SneakyThrows
    protected void appendToProject(@Nonnull String source, String destination) {
        Files.write(new File(projectDir, destination).toPath(), Files.readAllBytes(Paths.get(source)), StandardOpenOption.APPEND);
    }
}
