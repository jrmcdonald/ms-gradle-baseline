package com.jrmcdonald.common.baseline.util;

import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectUtils {

    public static boolean isRootProject(Project project) { return project.getRootProject().equals(project); }

    public static <T extends Task> T findTaskByName(Project project, String taskName, Class<T> taskClass) {
        return taskClass.cast(Objects.requireNonNull(project.getTasks().findByName(taskName)));
    }
}
