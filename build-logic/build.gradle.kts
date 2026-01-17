plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "BuildPluginJvm"
        }
        register("build-kmp") {
            id = "build-kmp"
            implementationClass = "BuildPluginKmp"
        }
    }
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(files(project.libs.javaClass.superclass.protectionDomain.codeSource.location))
}