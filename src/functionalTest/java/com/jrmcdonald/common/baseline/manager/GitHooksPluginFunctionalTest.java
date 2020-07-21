package com.jrmcdonald.common.baseline.manager;

import com.jrmcdonald.common.baseline.AbstractGradleFunctionalTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GitHooksPluginFunctionalTest extends AbstractGradleFunctionalTest {

    @Test
    @DisplayName("Can execute `installGitHooks` task")
    void canExecuteInstallGitHooksTask() {
        var result = build("installGitHooks", "-m");

        assertThat(result.getOutput()).doesNotContain("Something went wrong while installing your Git hooks")
                                      .contains(":installGitHooks SKIPPED")
                                      .contains("BUILD SUCCESSFUL");
    }
}
