plugins {
    kotlin("multiplatform") version ("1.4.10")
    kotlin("plugin.serialization") version "1.4.10"
}
repositories {
    mavenCentral()
    jcenter()
}
kotlin {
    macosX64("macos") {
        binaries {
            executable {
                // Change to specify fully qualified name of your application's entry point:
                entryPoint = "wu.seal.tools.commandline.main"
                // Specify command-line arguments, if necessary:
//                runTask?.args("")
            }
            compilations["main"].cinterops {
                create("scriptexec") {
                    packageName = "sagiegurari.c_scriptexec"
                    defFile = file("$projectDir/scriptexec/scriptexec.def")
                    includeDirs("$projectDir/scriptexec/include")
                }
            }
        }
    }
    sourceSets {
        // Note: To enable common source sets please comment out 'kotlin.import.noCommonSourceSets' property
        // in gradle.properties file and re-import your project in IDE.
        val macosMain by getting {
            dependencies {
                implementation("com.github.ajalt.clikt:clikt:3.0.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
            }
        }
        val macosTest by getting {
        }
    }
}

// Use the following Gradle tasks to run your application:
// :runReleaseExecutableMacos - without debug symbols
// :runDebugExecutableMacos - with debug symbols