//plugins {
//    id("build-jvm")
//}

plugins {
    alias(libs.plugins.kotlin.jvm)
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.datetime.my)
}
