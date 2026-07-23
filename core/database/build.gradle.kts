plugins {
    id("kmp-core")
    id("room")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.database"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.findLibrary("koin-core").get())
            }
        }
    }
}
