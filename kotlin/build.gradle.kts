plugins {
    kotlin("jvm") version "2.1.0"
}

group = "com.sheepduke.donkeyrace"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
