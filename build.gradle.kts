plugins {
    application
    kotlin("jvm") version "1.9.23"
}

group = "com.thedearbear"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.thedearbear.MainKt"
}

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    google()
}

dependencies {
    implementation("io.github.skylot:jadx-core:1.5.0-SNAPSHOT") {
        isChanging = true
    }

    implementation("io.github.skylot:jadx-dex-input:1.5.0-SNAPSHOT") {
        isChanging = true
    }

    implementation("io.github.skylot:jadx-smali-input:1.5.0-SNAPSHOT") {
        isChanging = true
    }

    implementation("org.slf4j:slf4j-simple:2.0.13")
}
kotlin {
    jvmToolchain(21)
}