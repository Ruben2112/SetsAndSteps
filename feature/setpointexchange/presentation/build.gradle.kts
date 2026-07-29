plugins {
    id("kmp-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.feature.setpointexchange.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.presentation)
                implementation(projects.core.designSystem)
                implementation(projects.core.navigation)
                implementation(projects.core.foundcards)

                implementation(libs.findLibrary("jetbrains-material-icons").get())
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.feature.setpointexchange.presentation.generated.resources"
}
