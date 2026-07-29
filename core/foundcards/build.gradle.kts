plugins {
    id("kmp-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.foundcards"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.designSystem)

                implementation(
                    libs
                        .findLibrary("jetbrains-material-icons")
                        .get(),
                )
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.core.foundcards.generated.resources"
}
