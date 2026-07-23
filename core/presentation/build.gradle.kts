plugins {
    id("kmp-core")
    id("compose-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.presentation"
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.findLibrary("androidx-health-connect").get())
                implementation(libs.findLibrary("androidx-activity-compose").get())
            }
        }
        commonMain {
            dependencies {
                implementation(projects.core.domain)
            }
        }
    }
}
