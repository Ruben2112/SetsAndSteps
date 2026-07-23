plugins {
    id("kmp-feature")
}

kotlin {
    android {
        namespace = "com.heveamobile.setsandsteps.feature.cards.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.designSystem)
                implementation(projects.core.navigation)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.heveamobile.setsandsteps.feature.cards.presentation.generated.resources"
}
