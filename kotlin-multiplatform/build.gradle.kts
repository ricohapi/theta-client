import java.util.*

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.7.20"
    id("com.android.library")
    id("maven-publish")
    id("org.jetbrains.dokka")
    kotlin("native.cocoapods")
    signing
    id("io.gitlab.arturbosch.detekt").version("1.19.0")
}

val theta_client_version = "1.0.0"
// Init publish property
initProp()

kotlin {
    android()

    cocoapods {
        summary = "THETA Client"
        homepage = "https://github.com/ricohapi/theta-client"
        name = "THETAClient"
        authors = "Ricoh Co, Ltd."
        version = theta_client_version
        source = "{ :http => 'https://github.com/ricohapi/theta-client/releases/download/${theta_client_version}/THETAClient.xcframework.zip' }"
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

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

// Publish the library to GitHub Packages Mavan repository.
// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {
    initProp()
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                // Applies the component for the release build variant.
                from(components["release"])
                artifact(javadocJar.get())
                groupId = "com.ricoh360.thetaclient"
                artifactId = "theta-client"
                version = theta_client_version
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
            create<MavenPublication>("debug") {
                // Applies the component for the debug build variant.
                from(components["debug"])
                groupId = "com.ricoh360.thetaclient"
                artifactId = "theta-client-debug"
                version = theta_client_version
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

//detekt {
//    ignoreFailures = false
//    buildUponDefaultConfig = true // preconfigure defaults
//    allRules = false // activate all available (even unstable) rules.
//    config = files("$rootDir/config/detekt.yml") // チェック項目の設定ファイル
//    baseline = file("$rootDir/config/baseline.xml")
//    input = files("$projectDir/src/jp/co/nttcom/hourensou/") // 対象フォルダ
//}

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
