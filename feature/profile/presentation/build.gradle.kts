plugins {
    id("kmp-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.feature.profile.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.presentation)
                implementation(projects.core.designSystem)
                implementation(projects.core.navigation)

                implementation(libs.findLibrary("kotlinx-datetime").get())

                implementation(libs.findLibrary("vico-compose").get())
                implementation(libs.findLibrary("vico-compose-m3").get())
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.feature.profile.presentation.generated.resources"
}
