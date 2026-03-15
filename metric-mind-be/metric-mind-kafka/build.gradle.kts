plugins {
    application
    alias(libs.plugins.build.jvm)
    alias(libs.plugins.shadow)
}

application {
    mainClass.set("MainKt")
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)
    implementation(libs.logback)

    implementation(projects.metricMindCommon)
    implementation(projects.metricMindBiz)
    implementation(projects.metricMindApiV1)
    implementation(projects.metricMindApiV1Mapper)
    implementation(projects.metricMindLogger)

    testImplementation(kotlin("test-junit"))
}