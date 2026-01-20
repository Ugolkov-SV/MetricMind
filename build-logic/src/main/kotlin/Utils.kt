import org.gradle.api.Project

fun Project.applyGroupAndVersion() {
    group = rootProject.group
    version = rootProject.version
}