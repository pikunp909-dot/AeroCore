plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.aerocore"
    compileSdk = 34
    ndkVersion = "25.2.9519653"

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17 -frtti -fexceptions"
                arguments += "-DANDROID_STL=c++_shared"
            }
        }

        ndk {
            abiFilters.addAll(setOf("arm64-v8a", "armeabi-v7a"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    // ============================================
    // BLACKBOX CORE — YEH ZAROORI HAI
    // ============================================
    implementation(project(":blackbox-core"))

    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // BouncyCastle (Ed25519)
    implementation("org.bouncycastle:bcprov-jdk18on:1.77")

    // JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
