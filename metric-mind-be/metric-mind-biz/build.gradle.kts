plugins {
    alias(libs.plugins.build.jvm)
}

dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(projects.metricMindCommon)
    implementation(projects.metricMindStubs)
    implementation(projects.metricMindLogger)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.coroutines.test)
}