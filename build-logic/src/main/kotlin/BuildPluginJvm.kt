import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class BuildPluginJvm : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            applyGroupAndVersion()
        }
}