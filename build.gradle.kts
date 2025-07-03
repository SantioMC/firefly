plugins {
    `java-library`
    `maven-publish`
}

allprojects {
    group = "me.santio"
    version = "1.0.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }
}