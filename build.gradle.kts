plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("8.13.0").apply(false)
    kotlin("multiplatform").version("2.2.21").apply(false)
    kotlin("plugin.serialization").version("2.2.21").apply(false)
    id("org.jetbrains.dokka").version("2.0.0")
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:versioning-plugin:2.0.0")
    }
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/dokka/dev")
}

tasks.register("clean", Delete::class) {
}
