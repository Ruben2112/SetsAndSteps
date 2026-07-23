import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.google.devtools.ksp")
    id("androidx.room")
}

extensions.configure<KotlinMultiplatformExtension> {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.findLibrary("androidx-room-runtime").get())
                api(libs.findLibrary("androidx-sqlite-bundled").get())
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.findLibrary("androidx-room-compiler").get())
    add("kspIosArm64", libs.findLibrary("androidx-room-compiler").get())
    add("kspIosSimulatorArm64", libs.findLibrary("androidx-room-compiler").get())
}

room {
    schemaDirectory("$projectDir/schemas")
}
