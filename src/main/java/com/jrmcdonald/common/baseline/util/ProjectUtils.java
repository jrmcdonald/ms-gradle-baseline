package com.jrmcdonald.common.baseline.util;

import org.gradle.api.Project;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectUtils {

    public static boolean isRootProject(Project project) { return project.getRootProject().equals(project); }

}
