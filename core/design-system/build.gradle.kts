plugins {
    id("kmp-core")
    id("compose-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.designsystem"
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.findLibrary("mapbox-maps").get())
                implementation(libs.findLibrary("mapbox-maps-compose").get())
            }
        }
        commonMain {
            dependencies {
                implementation(projects.core.domain)

                implementation(libs.findLibrary("jetbrains-material-icons").get())

                implementation(libs.findLibrary("coil-compose").get())
                implementation(libs.findLibrary("coil-network").get())
                implementation(libs.findLibrary("coil-svg").get())
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.core.designsystem.generated.resources"
    publicResClass = true
}
