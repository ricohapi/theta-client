import java.util.Properties

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "2.2.10"
    id("com.android.library")
    id("maven-publish")
    kotlin("native.cocoapods")
    signing
    id("io.gitlab.arturbosch.detekt").version("1.23.3")
}

dependencies {
}

val thetaClientVersion = "1.12.1"
group = "com.ricoh360.thetaclient"
version = thetaClientVersion

// Init publish property
initProp()

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

    cocoapods {
        summary = "THETA Client"
        homepage = "https://github.com/ricohapi/theta-client"
        name = "THETAClient"
        authors = "Ricoh Co, Ltd."
        version = thetaClientVersion
        source =
            "{ :http => 'https://github.com/ricohapi/theta-client/releases/download/$thetaClientVersion/THETAClient.xcframework.zip' }"
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
        val ktorVersion = "3.1.1"
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
    compileSdk = 36
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        setProperty("archivesBaseName", "theta-client")
        consumerProguardFiles("proguard-rules.pro")
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

// Publish the library to GitHub Packages Mavan repository.
// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {
    initProp()
    publishing {
        publications.withType(MavenPublication::class) {
            artifact(javadocJar.get())
            when (name) {
                "androidRelease" -> {
                    artifactId = "theta-client"
                }

                else -> {
                    artifactId = "theta-client-$name"
                }
            }
            pom {
                name.set("theta-client")
                description.set("This library provides a way to control RICOH THETA using RICOH THETA API v2.1")
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
        }
        repositories {
            maven {
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = getExtraString("ossrhUsername")
                    password = getExtraString("ossrhPassword")
                }
            }
        }
    }
}

signing {
    if (getExtraString("signing.keyId") != null) {
        useInMemoryPgpKeys(
            getExtraString("signing.keyId"),
            getExtraString("signing.key"),
            getExtraString("signing.password")
        )
        sign(publishing.publications)
    }
}

detekt {
    ignoreFailures = false
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$rootDir/config/detekt.yml") // config file
    baseline = file("$rootDir/config/baseline.xml")
    source = files(
        "$rootDir/kotlin-multiplatform/src/commonMain/"
    ) // the folders to be checked
}

ext["signing.keyId"] = null
ext["signing.key"] = null
ext["signing.password"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

fun initProp() {
    val secretPropsFile = project.rootProject.file("local.properties")
    if (secretPropsFile.exists()) {
        secretPropsFile.reader().use {
            Properties().apply {
                load(it)
            }
        }.onEach { (name, value) ->
            ext[name.toString()] = value
        }
    } else {
        ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
        ext["signing.key"] = System.getenv("SIGNING_KEY")
        ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
        ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
        ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    }
}

fun getExtraString(name: String): String? {
    if (ext.has(name)) {
        return ext[name]?.toString()
    }
    return null
}