pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    if (System.getenv("IS_CI") == "yes") {
        repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    } else {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    }
    repositories {
        mavenCentral()
        google()
        maven("https://jitpack.io/")
    }
}

rootProject.name = "kedis"

include(":kedis")

if (System.getenv("IS_CI") != "yes") {
    include(":example")
}
