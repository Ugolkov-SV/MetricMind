import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.hotReload)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidLibrary {
        namespace = "io.ugolkov.metric_mind.composeApp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    if (Os.isFamily(Os.FAMILY_MAC)) {
        add("kspIosSimulatorArm64", libs.androidx.room.compiler)
        add("kspIosArm64", libs.androidx.room.compiler)
    }
}

compose.desktop {
    application {
        mainClass = "io.ugolkov.metric_mind.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.ugolkov.metric_mind"
            packageVersion = "1.0.0"
        }
    }
}

room {
    schemaDirectory("dbSchemas")
}