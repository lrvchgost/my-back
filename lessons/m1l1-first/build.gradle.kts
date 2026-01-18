plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

//tasks.register("runApp") {
//    println("hello")
//    task(":m1l1-first:run")
////    dependsOn(gradle.includedBuild("my-app").task(":app:run"))
//}