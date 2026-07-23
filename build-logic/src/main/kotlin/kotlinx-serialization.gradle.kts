import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

extensions.configure<KotlinMultiplatformExtension> {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx-serialization-core").get())
                implementation(libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}
