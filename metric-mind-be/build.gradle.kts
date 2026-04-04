plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.openapi.generator) apply false
}

group = "io.ugolkov"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-v1.yaml").toString())
}

tasks.register("allProjectTest", Test::class) {
    group = "verification"

    subprojects.flatMap { it.tasks }
        .filter { it.group == "verification" }
        .filter { it.name.endsWith(suffix = "test", ignoreCase = true) }
        .forEach { task ->
            dependsOn(task)
        }
}