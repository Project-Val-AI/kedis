plugins {
    `maven-publish`
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kover) apply false
    id("net.linguica.maven-settings") version "0.5"
}

subprojects {
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "net.linguica.maven-settings")

    publishing {

        repositories {
            maven {
                name = "fyreGames"
                url = uri("https://maven.fyre.services/games")
            }
        }


    }
}
