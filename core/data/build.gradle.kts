plugins {
    id("kmp-core")
    id("koin")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.data"
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.findLibrary("koin-android").get())
                implementation(libs.findLibrary("androidx-health-connect").get())
                implementation(
                    libs
                        .findLibrary("ktor-client-okhttp")
                        .get(),
                )
            }
        }
        iosMain {
            dependencies {
                implementation(
                    libs
                        .findLibrary("ktor-client-darwin")
                        .get(),
                )
            }
        }
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.database)

                implementation(libs.findLibrary("androidx-datastore").get())
                implementation(libs.findLibrary("androidx-datastore-preferences").get())
                implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                implementation(libs.findLibrary("kotlinx-datetime").get())

                implementation(
                    libs
                        .findLibrary("supabase-postgrest")
                        .get(),
                )
            }
        }
    }
}
