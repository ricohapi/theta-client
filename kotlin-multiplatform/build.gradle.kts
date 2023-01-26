plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.7.20"
    id("com.android.library")
    id("maven-publish")
    id("org.jetbrains.dokka")
    kotlin("native.cocoapods")
}

kotlin {
    android()

    cocoapods {
        summary = "THETA Client"
        homepage = "https://theta360.com/"
        name = "THETAClient"
        authors = "Ricoh Co, Ltd."
        version = "1.0"
        source = "{ :http => ''}"
        license = "MIT"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "THETAClient"
            isStatic = false
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val coroutines_version = "1.6.4"
        val coroutines_mtversion = "1.6.4-native-mt"
        val ktor_version = "2.1.2"
        val logback_version = "1.4.4"

        val commonMain by getting {
            dependencies {
                // Works as common dependency as well as the platform one
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_mtversion")
                api("io.ktor:ktor-client-core:$ktor_version") // Applications need to use ByteReadPacket class
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
                implementation("io.ktor:ktor-client-mock:$ktor_version")
                implementation("com.goncalossilva:resources:0.2.2")
            }
        }
        val androidMain by getting
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 32
        setProperty("archivesBaseName", "theta-client")
    }
}

// Publish the library to GitHub Packages Mavan repository.
// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                // Applies the component for the release build variant.
                from(components["release"])
                groupId = "ricoh360.com"
                artifactId = "theta-client"
                version = "0.1"
            }
            // Creates a Maven publication called “debug”.
            create<MavenPublication>("debug") {
                // Applies the component for the debug build variant.
                from(components["debug"])
                groupId = "ricoh360.com"
                artifactId = "theta-client-debug"
                version = "0.1"
            }
        }
        repositories {
            maven {
                url = uri("https://maven.pkg.github.com/ricohapi/theta-client")
                credentials {
                    username = System.getenv("GITHUB_USER")
                    password = System.getenv("GITHUB_PAT") // Personal access token
                }
            }
        }
    }
}
