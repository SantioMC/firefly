plugins {
    `java-gradle-plugin`

    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("com.vanniktech.maven.publish") version "0.33.0"
}

group = findProperty("group")!!
version = findProperty("version")!!

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
    compileOnly("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.20-2.0.1")
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

kotlin {
    jvmToolchain(21)
}

publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/santiomc/firefly")
            credentials {
                username = findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "${project.group}.firefly",
        artifactId = "plugin",
        version = findProperty("version") as String
    )
}