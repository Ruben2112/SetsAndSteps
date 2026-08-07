import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.mapbox.maps)
    implementation(libs.mapbox.maps.compose)
}

android {
    namespace = "com.heveamobile.setsandsteps"
    compileSdk = libs.versions.android.compileSdk
        .get()
        .toInt()

    defaultConfig {
        applicationId = "com.heveamobile.setsandsteps"
        minSdk = libs.versions.android.minSdk
            .get()
            .toInt()
        targetSdk = libs.versions.android.targetSdk
            .get()
            .toInt()
        versionName = "0.1.0" // [MAJOR].[MINOR].[PATCH]
        versionCode = 1000 // 000.001.000 (version name without separators and leading zeroes)
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = false
            // TODO: replace with a dedicated release signingConfig once a production keystore exists.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}