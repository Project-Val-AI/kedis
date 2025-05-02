import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {
        withJava()
        mainRun {
            mainClass.set("MainKt")
        }
    }
    addNativeTargets {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kedis"))
                implementation(libs.kop)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.kotlin.logging)
            }
        }
        val nativeMain by creating {}
        val jvmMain by getting {
            dependencies {
                implementation(libs.logback)
            }
        }
    }
}

fun KotlinMultiplatformExtension.addNativeTargets(
    block: KotlinNativeTarget.() -> Unit,
) {
    linuxX64 {
        block()
    }
    linuxArm64 {
        block()
    }
    macosX64 {
        block()
    }
    macosArm64 {
        block()
    }
    mingwX64 {
        block()
    }
}
