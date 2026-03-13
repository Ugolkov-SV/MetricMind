plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.build.jvm)
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.kotlinx.serialization)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(kotlin("reflect"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.datetime)

    implementation(projects.metricMindCommon)
    implementation(projects.metricMindApiV1)
    implementation(projects.metricMindStubs)
    implementation(projects.metricMindLogger)

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1")
            .map { rootProject.ext[it] }

        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}