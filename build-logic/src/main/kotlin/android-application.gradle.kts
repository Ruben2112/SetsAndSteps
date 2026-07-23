plugins {
    id("com.android.application")
}

android {
    compileSdk = libs.int("android-compileSdk")

    defaultConfig {
        minSdk = libs.int("android-minSdk")
        targetSdk = libs.int("android-targetSdk")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
