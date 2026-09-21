buildscript {
    ext {
        compileSdkVersion = 34
        minSdk = 21
        targetSdk = 34
        ndkVersion = "25.2.9519653"
    }
}

plugins {
    id("com.android.library") version "8.2.0" apply false
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
