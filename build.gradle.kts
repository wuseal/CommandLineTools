plugins {
    kotlin("multiplatform") version ("1.4.10")
    kotlin("plugin.serialization") version "1.4.10"
}
repositories {
    mavenCentral()
    jcenter()
}
kotlin {
    macosX64 {
        binaries {
            executable {
                entryPoint = "wu.seal.tools.commandline.main"
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
        val macosX64Main by getting {
            dependencies {
                implementation("com.github.ajalt.clikt:clikt:3.0.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
            }
        }
    }
}

