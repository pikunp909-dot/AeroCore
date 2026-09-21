// Define shared versions for all modules
extra["compileSdkVersion"] = 34
extra["minSdk"] = 21
extra["targetSdk"] = 34
extra["ndkVersion"] = "25.2.9519653"

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
        classpath("com.github.kezong:fat-aar:1.3.9")  // ← Fat AAR plugin
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
