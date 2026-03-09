import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class BuildPluginKmp : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")

            applyGroupAndVersion()

            plugins.withId("org.jetbrains.kotlin.multiplatform") {
                extensions.configure<KotlinMultiplatformExtension> {
                    configureTargets(target)
                    sourceSets.configureEach {
                        languageSettings.apply {
                            languageVersion = "2.3"
                            progressiveMode = true
                            optIn("kotlin.time.ExperimentalTime")
                        }
                    }
                }
            }
        }

    private fun KotlinMultiplatformExtension.configureTargets(project: Project) {
        jvm()

        val libs = project.the<LibrariesForLibs>()
        project.tasks.withType(JavaCompile::class.java) {
            sourceCompatibility = libs.versions.jvm.language.get()
            targetCompatibility = libs.versions.jvm.compiler.get()
        }
    }
}