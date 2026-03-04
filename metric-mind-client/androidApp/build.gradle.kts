import com.android.build.api.variant.impl.VariantOutputImpl
import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "io.ugolkov.metric_mind"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.ugolkov.metric_mind"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    signingConfigs {
        create("prod") {
            storeFile = file("../configs/keystore.jks")
            storePassword = getLocalProperty("storePassword")
            keyAlias = getLocalProperty("keyAliasProd")
            keyPassword = getLocalProperty("keyPasswordProd")
        }
    }

    flavorDimensions += "type"
    productFlavors {
        create("dev") {
            dimension = "type"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("prod") {
            dimension = "type"
            signingConfig = this@android.signingConfigs.getByName("prod")
        }
    }

    //noinspection WrongGradleMethod
    androidComponents {
        onVariants { variant ->
            variant.outputs
                .filterIsInstance<VariantOutputImpl>()
                .forEach { output ->
                    output.outputFileName = "MetricMind_v${output.versionName.get()}.apk"
                }
        }
    }
}

dependencies {
    implementation(project(":composeApp"))

    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.compose.uiTooling)
}