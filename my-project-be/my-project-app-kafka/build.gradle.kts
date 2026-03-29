plugins {
    application
    id("build-jvm")
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.lrvch.app.kafka.MainKt")
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation(libs.cofs.logs.logback)

    implementation(project(":my-project-app-common"))

    // transport models
    implementation(project(":my-project-common"))
    implementation(project(":my-project-api-v1-jackson"))
    implementation(project(":my-project-api-v1-mappers"))
    implementation(project(":my-project-api-v2-kmp"))
    // logic
    implementation(project(":my-project-biz"))

    testImplementation(kotlin("test-junit"))
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }
}

