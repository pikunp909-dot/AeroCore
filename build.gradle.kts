// Define shared versions for all modules
extra["compileSdkVersion"] = 34
extra["minSdk"] = 21
extra["targetSdk"] = 34
extra["ndkVersion"] = "25.2.9519653"

plugins {
    id("com.android.library") version "8.2.0" apply false
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
