plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

group = "io.ugolkov"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version
}