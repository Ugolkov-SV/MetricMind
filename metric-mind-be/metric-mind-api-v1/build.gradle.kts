plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.kotlinx.serialization)
}

openApiGenerate {
    generatorName.set("kotlin")
    val version = "v1"
    val openapiGroup = "${rootProject.group}.api.$version"
    packageName.set(openapiGroup)
    modelPackage.set("$openapiGroup.models")
    val specsDir = rootProject.layout.projectDirectory.dir("../specs")
    val specsFile = specsDir.file("specs-$version.yaml")
    inputSpec.set(specsFile.toString())

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
        put("modelTests", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "kotlinx-datetime",
            "enumPropertyNaming" to "UPPERCASE",
            "collectionType" to "list",
            "library" to "multiplatform",
        )
    )
}

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(layout.buildDirectory.dir("generate-resources/main/src"))
            dependencies {
                implementation(projects.metricMindCommon)

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
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

tasks {
    filter { task ->
        task.name.startsWith("compile")
    }
        .forEach { task ->
            task.dependsOn(openApiGenerate)
        }
}