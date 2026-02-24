plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.otus.otuskotlin.lrvc"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }
    register("buildInfra") { ->
        dependsOn(
            gradle.includedBuild("deploy-project").task(":buildInfra")
        )
    }

//    register("buildImages") {
//        dependsOn(gradle.includedBuild("ok-marketplace-be").task(":buildImages"))
//    }
    register("e2eTests") { ->
        dependsOn(
            gradle.includedBuild("my-project-tests").task(":e2eTests")
        )
    }

    register("check") {
        group = "verification"
        dependsOn(gradle.includedBuild("my-project-be").task(":check"))
    }
}