import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("kmp-core")
    id("compose-feature")
    id("koin")
    id("kotlinx-serialization")
}

extensions.configure<KotlinMultiplatformExtension> {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("jetbrains-navigation3-ui").get())
                implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-nav3").get())
                implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
            }
        }
    }
}
