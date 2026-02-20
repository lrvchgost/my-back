plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.myProjectApiV1Jackson)
    implementation(projects.myProjectCommon)

    testImplementation(kotlin("test-junit"))
//    testImplementation(projects.myProjectStubs)
}
