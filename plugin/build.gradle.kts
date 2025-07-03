plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "2.1.20"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
    compileOnly("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.20-2.0.1")
    implementation("com.gradleup.shadow:shadow-gradle-plugin:8.3.7")
}

gradlePlugin {
    plugins {
        create("firefly") {
            id = "me.santio.firefly"
            implementationClass = "me.santio.firefly.plugin.FireflyPlugin"
        }
    }
}

kotlin {
    jvmToolchain(21)
}