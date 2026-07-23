plugins {
    id("kmp-core")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.domain"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("compose-runtime").get())
                implementation(libs.findLibrary("compose-components-resources").get())
                implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                implementation(libs.findLibrary("kotlinx-datetime").get())
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.core.domain.generated.resources"
}
