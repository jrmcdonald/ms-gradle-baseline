package com.jrmcdonald.common.baseline.util;

import org.gradle.api.Project;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.lang.String.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskPathUtils {

    public static String getRootProjectPath(String task) {
        return format(":%s", task);
    }

    public static String getSubProjectPath(Project subProject, String task) {
        return format(":%s:%s", subProject.getName(), task);
    }

}
