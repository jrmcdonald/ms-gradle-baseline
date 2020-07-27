package com.jrmcdonald.common.baseline.core.jacoco;

import org.gradle.api.Task;
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JacocoUtils {

    public static boolean hasJacocoTaskExtension(Task task) { return task.getExtensions().findByType(JacocoTaskExtension.class) != null; }

}
