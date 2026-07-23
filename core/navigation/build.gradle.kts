plugins {
    id("kmp-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.core.navigation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.designSystem)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.core.navigation.generated.resources"
}
