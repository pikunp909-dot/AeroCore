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
    implementation(project(":blackbox-core"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("org.bouncycastle:bcprov-jdk18on:1.77")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

tasks.register("mergeBcoreIntoAar") {
    dependsOn(":aerocore:assembleRelease", ":blackbox-core:bundleReleaseAar")
    doLast {
        val outputDir = file("$buildDir/outputs/aar")
        val aerocoreAar = file("$outputDir/aerocore-release.aar")

        val bcoreAarFiles = fileTree(rootDir.resolve("blackbox-core/Bcore/build/outputs/aar")).matching { include("*.aar") }.files
        if (bcoreAarFiles.isEmpty()) {
            throw GradleException("Blackbox-core AAR not found!")
        }
        val bcoreAar = bcoreAarFiles.first()

        val tmpDir = file("$buildDir/tmp/mergeBcore")
        if (tmpDir.exists()) tmpDir.deleteRecursively()

        val aerocoreUnzip = file("$tmpDir/aerocore")
        val bcoreUnzip = file("$tmpDir/bcore")
        val mergedClasses = file("$tmpDir/merged_classes")

        aerocoreUnzip.mkdirs()
        bcoreUnzip.mkdirs()
        mergedClasses.mkdirs()

        ant.withGroovyBuilder {
            "unzip"("src" to aerocoreAar, "dest" to aerocoreUnzip)
            "unzip"("src" to bcoreAar, "dest" to bcoreUnzip)
        }

        val aerocoreClassesJar = file("$aerocoreUnzip/classes.jar")
        val bcoreClassesJar = file("$bcoreUnzip/classes.jar")

        if (aerocoreClassesJar.exists()) {
            ant.withGroovyBuilder {
                "unzip"("src" to aerocoreClassesJar, "dest" to mergedClasses)
            }
        }
        if (bcoreClassesJar.exists()) {
            ant.withGroovyBuilder {
                "unzip"("src" to bcoreClassesJar, "dest" to mergedClasses)
            }
        }

        file("$mergedClasses/META-INF").deleteRecursively()

        if (aerocoreClassesJar.exists()) {
            aerocoreClassesJar.delete()
        }

        ant.withGroovyBuilder {
            "jar"("destfile" to aerocoreClassesJar, "basedir" to mergedClasses)
        }

        val bcoreJni = file("$bcoreUnzip/jni")
        val aerocoreJni = file("$aerocoreUnzip/jni")
        if (bcoreJni.exists()) {
            bcoreJni.copyRecursively(aerocoreJni, overwrite = true)
        }

        val bcoreAssets = file("$bcoreUnzip/assets")
        val aerocoreAssets = file("$aerocoreUnzip/assets")
        if (bcoreAssets.exists()) {
            bcoreAssets.copyRecursively(aerocoreAssets, overwrite = true)
        }

        val mergedAar = file("$outputDir/aerocore-release-merged.aar")
        if (mergedAar.exists()) mergedAar.delete()

        ant.withGroovyBuilder {
            "zip"("destfile" to mergedAar, "basedir" to aerocoreUnzip)
        }

        println("Successfully generated merged AAR at: $mergedAar")
    }
}
