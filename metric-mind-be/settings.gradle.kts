pluginManagement {
    includeBuild("../build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "metric-mind-be"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

//include("metric-mind-tmp")
include("metric-mind-common")
include("metric-mind-api-v1")
include("metric-mind-stubs")
include("metric-mind-spring")
include("metric-mind-logger")