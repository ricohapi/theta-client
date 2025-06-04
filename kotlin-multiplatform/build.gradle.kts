import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.20"
    id("com.android.library")
    id("org.jetbrains.dokka") version "1.9.20"
    kotlin("native.cocoapods")
    signing
    id("io.gitlab.arturbosch.detekt").version("1.23.3")
    id("com.vanniktech.maven.publish") version "0.32.0"
}

dependencies {
    dokkaPlugin("org.jetbrains.dokka:versioning-plugin:1.9.20")
}

val thetaClientVersion = "1.12.1"
group = "com.ricoh360.thetaclient"
version = thetaClientVersion

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release")
    }

    cocoapods {
        summary = "THETA Client"
        homepage = "https://github.com/ricohapi/theta-client"
        name = "THETAClient"
        authors = "Ricoh Co, Ltd."
        version = thetaClientVersion
        source = "{ :http => 'https://github.com/ricohapi/theta-client/releases/download/${thetaClientVersion}/THETAClient.xcframework.zip' }"
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
        val coroutinesVersion = "1.7.3"
        val ktorVersion = "2.3.13"
        val kryptoVersion = "4.0.10"

        val commonMain by getting {
            dependencies {
                // Works as common dependency as well as the platform one
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
                api("io.ktor:ktor-client-core:$ktorVersion") // Applications need to use ByteReadPacket class
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("com.soywiz.korlibs.krypto:krypto:$kryptoVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("com.goncalossilva:resources:0.4.0")
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
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
    namespace = "com.ricoh360.thetaclient"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        setProperty("archivesBaseName", "theta-client")
        consumerProguardFiles("proguard-rules.pro")
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

// Publish the library to Mavan repository.
// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {
    mavenPublishing {
        // publishing to https://central.sonatype.com/
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
        coordinates(group.toString(), "theta-client", version.toString())
        pom {
            name.set("theta-client")
            description.set("This library provides a way to control RICOH THETA using RICOH THETA API v2.1")
            inceptionYear.set("2023")
            url.set("https://github.com/ricohapi/theta-client")
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://github.com/ricohapi/theta-client/blob/main/LICENSE")
                }
            }
            developers {
                developer {
                    organization.set("RICOH360")
                    organizationUrl.set("https://github.com/ricohapi/theta-client")
                }
            }
            scm {
                connection.set("scm:git:git@github.com:ricohapi/theta-client.git")
                developerConnection.set("scm:git:git@github.com:ricohapi/theta-client.git")
                url.set("https://github.com/ricohapi/theta-client/tree/main")
            }
        }
        /* Secrets
         *     Set following environment variables for Central Portal user token
         *       * ORG_GRADLE_PROJECT_mavenCentralUsername: username of the user token of Central Portal
         *       * ORG_GRADLE_PROJECT_mavenCentralPassword: password of the user token of Central Portal
         *     Set following environment variables for GPG key. See https://vanniktech.github.io/gradle-maven-publish-plugin/central/#secrets
         *       * ORG_GRADLE_PROJECT_signingInMemoryKey : Secret key in PEM format
         *       * ORG_GRADLE_PROJECT_signingInMemoryKeyId : 8 characters key id
         *       * ORG_GRADLE_PROJECT_signingInMemoryKeyPassword
         */
    }
}

detekt {
    ignoreFailures = false
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$rootDir/config/detekt.yml") // config file
    baseline = file("$rootDir/config/baseline.xml")
    source = files(
        "$rootDir/kotlin-multiplatform/src/commonMain/",
        "$rootDir/flutter/android/src/",
        "$rootDir/react-native/android/src/"
    ) // the folders to be checked
}

tasks.dokkaHtml.configure {
    moduleName.set("theta-client")

    if (project.properties["version"].toString() != thetaClientVersion) {
        throw GradleException("The release version does not match the version defined in Gradle.")
    }

    val pagesDir = file(project.properties["workspace"].toString()).resolve("gh-pages")
    val currentVersion = thetaClientVersion
    val currentDocsDir = pagesDir.resolve("docs")
    val docVersionsDir = pagesDir.resolve("version")
    outputDirectory.set(currentDocsDir)

    pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        version = currentVersion
        olderVersionsDir = docVersionsDir
    }

    doLast {
        val storedDir = docVersionsDir.resolve(currentVersion)
        currentDocsDir.copyRecursively(storedDir)
        storedDir.resolve("older").deleteRecursively()
    }
}
