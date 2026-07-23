plugins {
    id("kmp-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.feature.settings.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.presentation)
                implementation(projects.core.designSystem)
                implementation(projects.core.navigation)

                implementation(libs.findLibrary("kotlinx-datetime").get())
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources"
}
