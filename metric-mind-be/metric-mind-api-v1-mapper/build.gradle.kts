plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.metricMindCommon)
                implementation(projects.metricMindApiV1)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.metricMindStubs)
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}