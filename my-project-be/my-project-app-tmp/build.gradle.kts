plugins {
    id("build-jvm")
    application
}

//application {
//    mainClass.set("ru.otus.otuskotlin.lrvch.app.tmp.MainKt")
//}

dependencies {
//    implementation(project(":my-project-api-logv1"))
    implementation(projects.myProjectApiLogv1)
    implementation("ru.otus.otuskotlin.lrvch.libs:ok-marketplace-lib-logging-common")
    implementation("ru.otus.otuskotlin.lrvch.libs:ok-marketplace-lib-logging-logback")

//    implementation(project(":my-project-common"))
    implementation(projects.myProjectCommon)

    implementation(libs.coroutines.core)
}
