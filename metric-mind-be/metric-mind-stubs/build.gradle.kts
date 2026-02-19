plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.metricMindCommon)
            }
        }
    }
}