plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)

    id("me.santio.firefly")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":paper"))
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
    ksp(project(":processor"))
}

firefly {
    includeDependencies = false
}