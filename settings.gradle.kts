plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "MetricMind"

includeBuild("lessons")
includeBuild("metric-mind-be")