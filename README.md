
# ms-gradle-baseline ![build](https://github.com/jrmcdonald/ms-gradle-baseline/workflows/build/badge.svg)

## Overview

A gradle plugin to apply a common baseline of plugins to a java project.

## Features

| Feature                                                                        | Implemented |
|--------------------------------------------------------------------------------|-------------|
| Dependency version checking via [Gradle Versions](#dependencyupdates-plugin)   | 👍          |
| OWASP CVE checking via [Dependency-Check](#dependencycheck-plugin)             | 👍          |
| Static analysis with [Spotbugs](#spotbugs-plugin)                              | 👍          |
| Test convorage with [Jacoco](#jacoco-plugin)                                   | 👍          |
| Spring Boot features via [Spring Boot](#spring-boot-plugin)                    | 👍          |
| Git Hook configuration via [GitHooks](#githooks-plugin)                        | 👍          |
| Configure third party maven repositories                                       | ⬜          |
| Setup common reusable gradle configurations                                    | ⬜          |
| Configure gradle to use JUnit 5                                                | ⬜          |
| Configure Java compiler options                                                | ⬜          |
| Configure IDE settings (code style/editorconfig)                               | ⬜          |

## Usage

In your `build.gradle` add the following line to the plugin section:
```
plugins {
  ...
  id "com.jrmcdonald.ms-gradle-baseline" version "1.0.0"
  ...
}
```
The latest version can be found by going to https://plugins.gradle.org/plugin/com.jrmcdonald.ms-gradle-baseline.

## Plugins

The configuration applied by this plugin is the equivalent to the following:

### DependencyUpdates Plugin

Check for the latest versions of any gradle dependencies using the [Gradle Versions Plugin](https://github.com/ben-manes/gradle-versions-plugin).

```groovy
def isNonStable = { String version ->
    def stableKeyword = ['RELEASE', 'FINAL', 'GA'].any { it -> version.toUpperCase().contains(it) }
    def regex = /^[0-9,.v-]+(-r)?$/
    return !stableKeyword && !(version ==~ regex)
}

tasks {
    dependencyUpdates {
        rejectVersionIf {
            isNonStable(it.candidate.version)
        }
        checkConstraints = true
    }
}
tasks.build.dependsOn 'dependencyUpdates'
```

### DependencyCheck Plugin

Check for any vulnerabilities in any gradle dependencies using the [Dependency-Check Plugin](https://github.com/jeremylong/DependencyCheck).

```groovy
dependencyCheck {
    format = 'ALL'
    analyzers {
        nodeEnabled = false
    }
}
check.dependsOn dependencyCheckAggregate
```

### SpotBugs Plugin

Find bugs with static analysis using the [SpotBugs Plugin](https://github.com/spotbugs/spotbugs-gradle-plugin).

```groovy
spotbugsMain {
    reports {
        xml.enabled(false)
        html.enabled(true)
    }
}
```

### Jacoco Plugin

Check code coverage using the [Jacoco Plugin](https://www.eclemma.org/jacoco/).

```groovy
tasks.register("codeCoverageReport", JacocoReport) {
    subprojects { subproject ->
        subproject.plugins.withType(JacocoPlugin).configureEach {
            subproject.tasks.matching({ t -> t.extensions.findByType(JacocoTaskExtension) }).configureEach { testTask ->
                sourceSets subproject.sourceSets.main
                executionData(testTask)
            }
            subproject.tasks.matching({ t -> t.extensions.findByType(JacocoTaskExtension) }).forEach {
                rootProject.tasks.codeCoverageReport.dependsOn(it)
            }
        }
    }

    reports {
        xml.enabled true
        html.enabled true
    }
}
test.finalizedBy codeCoverageReport
check.dependsOn codeCoverageReport
```

### Spring Boot Plugin

Enable Spring Boot applications using the [Spring Boot Plugin](https://plugins.gradle.org/plugin/org.springframework.boot).

Note that this can be disabled through the plugin extension as follows:

```groovy
baseline {
    springBootEnabled = false
}
```

### SonarQube Plugin

Enable sonarqube reporting using the [SonarScanner Plugin](https://github.com/SonarSource/sonar-scanner-gradle) and [SonarCloud](https://sonarcloud.io).

```groovy
sonarqube {
    properties {
        property "sonar.projectKey", "jrmcdonald_{{rootProjectName}}"
        property "sonar.organization", "jrmcdonald"
        property "sonar.host.url", "https://sonarcloud.io"
        property "sonar.coverage.jacoco.xmlReportPaths", "${project.buildDir}/reports/jacoco/codeCoverageReport/codeCoverageReport.xml"
    }
}
tasks.sonarqube.mustRunAfter codeCoverageReport
```

### GitHooks Plugin

Apply version controlled git hooks using the [ghooks plugin](https://github.com/gtramontina/ghooks.gradle).