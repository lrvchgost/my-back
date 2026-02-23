plugins {
    id("build-jvm")
    id("maven-publish")
}

group = "ru.otus.otuskotlin.lrvch"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("buildInfra") {
        group = "build"
        dependsOn(project(":deploy-project-dcompose").getTasksByName("publish",false))
    }
}