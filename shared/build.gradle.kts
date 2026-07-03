import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    android {
        namespace = "com.gumamobile.mapbystep.shared"
        compileSdk = libs.versions.android.compileSdk
            .get()
            .toInt()
        minSdk = libs.versions.android.minSdk
            .get()
            .toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.mapbox.maps)
                implementation(libs.mapbox.maps.compose)
            }
        }
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)

                implementation(libs.koin.compose.navigation)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.jetbrains.navigation3.ui)
                implementation(libs.jetbrains.lifecycle.viewmodel.nav3)
                implementation(libs.jetbrains.lifecycle.viewmodel)
                implementation(libs.jetbrains.material.icons)

                implementation(libs.androidx.health.connect)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)

                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)

                implementation(libs.coil.compose)
                implementation(libs.coil.network)
                implementation(libs.coil.svg)

                implementation(libs.vico.compose)
                implementation(libs.vico.compose.m3)

                implementation(libs.material.kolor)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.time.ExperimentalTime")
    }
}


dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)

    add(
        "kspAndroid",
        libs.androidx.room.compiler,
    )
    add(
        "kspIosArm64",
        libs.androidx.room.compiler,
    )
    add(
        "kspIosSimulatorArm64",
        libs.androidx.room.compiler,
    )
}

room {
    schemaDirectory("$projectDir/schemas")
}
