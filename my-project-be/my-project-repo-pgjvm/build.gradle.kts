plugins {
    id("build-jvm")
}
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.myProjectCommon)
    api(projects.myProjectRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.uuid)

    implementation(libs.db.postgres)
    implementation(libs.bundles.exposed)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.myProjectRepoTests)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)

}
