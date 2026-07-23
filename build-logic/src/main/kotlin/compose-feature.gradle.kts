import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

extensions.configure<KotlinMultiplatformExtension> {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.findLibrary("compose-uiToolingPreview").get())
            }
        }
        commonMain {
            dependencies {
                implementation(libs.findLibrary("compose-runtime").get())
                implementation(libs.findLibrary("compose-foundation").get())
                implementation(libs.findLibrary("compose-material3").get())
                implementation(libs.findLibrary("compose-ui").get())
                implementation(libs.findLibrary("compose-components-resources").get())
                implementation(libs.findLibrary("compose-uiToolingPreview").get())
                implementation(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                implementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
            }
        }
    }
}
