import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

extensions.configure<KotlinMultiplatformExtension> {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("koin-compose-viewmodel").get())
                implementation(libs.findLibrary("koin-compose-navigation").get())
            }
        }
    }
}
