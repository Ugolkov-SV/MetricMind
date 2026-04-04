plugins {
    alias(libs.plugins.build.jvm)
}

dependencies {
    implementation(projects.metricMindCommon)
    implementation(projects.metricMindStubs)
    implementation(projects.metricMindLogger)

    implementation(libs.kotlinx.datetime)
    implementation(libs.cor)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.coroutines.test)
}