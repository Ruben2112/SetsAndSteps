plugins {
    id("kmp-core")
    id("room")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.database"

        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.findLibrary("koin-core").get())
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(
                    libs
                        .findLibrary("androidx-room-testing")
                        .get(),
                )
                implementation(
                    libs
                        .findLibrary("kotlin-test")
                        .get(),
                )
                implementation(
                    libs
                        .findLibrary("kotlinx-coroutines-core")
                        .get(),
                )
                implementation(
                    libs
                        .findLibrary("androidx-testExt-junit")
                        .get(),
                )
                implementation(
                    libs
                        .findLibrary("androidx-test-runner")
                        .get(),
                )
            }
        }
    }
}
