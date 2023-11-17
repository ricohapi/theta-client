plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("8.1.2").apply(false)
    kotlin("multiplatform").version("1.9.10").apply(false)
    kotlin("plugin.serialization").version("1.9.10").apply(false)
    id("org.jetbrains.dokka").version("1.9.10")
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:versioning-plugin:1.9.10")
    }
}

tasks.register("clean", Delete::class) {
}
