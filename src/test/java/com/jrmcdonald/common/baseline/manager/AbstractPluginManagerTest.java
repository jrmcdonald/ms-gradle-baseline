package com.jrmcdonald.common.baseline.manager;

import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Objects;

import javax.annotation.Nonnull;

public abstract class AbstractPluginManagerTest {

    @Nonnull
    protected <T> T findExtensionByType(Project project, Class<T> clazz) {
        return Objects.requireNonNull(project.getExtensions().findByType(clazz));
    }

    @Nonnull
    protected Task findTaskByPath(Project project, String path) {
        return Objects.requireNonNull(project.getTasks().findByPath(path));
    }
}
