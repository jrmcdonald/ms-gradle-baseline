# ms-gradle-baseline

A gradle plugin to apply a common baseline to a java project.

Goals:

* Apply default configuration for the applied plugins
    * Spotbugs
    * Jacoco (codeCoverageReport task with description)
    * Sonarqube
    * DependencyUpdates
    * DependencyCheck
    * Spring Boot (bootJar default false)
* Apply default configurations
    * add github packages repo
    * add commonPlatform configuration
    * useJunitPlatform
    * compileJava options
* Apply default configuration for Intellij IDEA
    * code style
    * editorconfig
