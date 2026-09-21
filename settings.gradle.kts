pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "AeroCore"
include(":aerocore")
include(":sample")

include(":blackbox-core")
project(":blackbox-core").projectDir = file("blackbox-core/Bcore")

include(":black-reflection")
project(":black-reflection").projectDir = file("blackbox-core/black-reflection")

include(":compiler")
project(":compiler").projectDir = file("blackbox-core/compiler")
