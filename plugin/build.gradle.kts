import java.util.*

plugins {
    `java-gradle-plugin`

    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("com.vanniktech.maven.publish") version "0.33.0"
    id("com.github.gmazzo.buildconfig") version "5.6.7"
}

val props = Properties().apply {
    load(file("../gradle.properties").inputStream())
}

group = props["group"].toString()
version = findProperty("version")
    ?.takeIf { it != "unspecified" }
    ?: props["version"].toString()

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.20-2.0.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
    implementation("com.gradleup.shadow:shadow-gradle-plugin:8.3.7")
}

gradlePlugin {
    plugins {
        create("firefly") {
            id = "me.santio.firefly"
            implementationClass = "me.santio.firefly.plugin.FireflyPlugin"
            displayName = "Firefly Gradle Plugin"
            description = "A helper plugin to create firefly layers for Minecraft servers"
            tags.set(listOf("minecraft", "firefly", "server", "framework"))
        }
    }
}

buildConfig {
    className("BuildConfig")
    packageName("me.santio.firefly.plugin")

    buildConfigField("VERSION", project.version.toString())
}

kotlin {
    jvmToolchain(21)
}

publishing {
    repositories {
        maven {
            name = "santioRepo"
            url = uri("https://repo.santio.me/repository/public/")
            credentials {
                username = findProperty("repo.santio.username") as String? ?: System.getenv("REPO_USER")
                password = findProperty("repo.santio.password") as String? ?: System.getenv("REPO_PASS")
            }
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "${project.group}.firefly",
        artifactId = "plugin",
        version = project.version as String,
    )
}