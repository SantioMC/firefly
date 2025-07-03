plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)

    id("me.santio.firefly")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":paper"))
    implementation(project(":game"))
    implementation(project(":config"))
    implementation(project(":config:pkl"))

    ksp(project(":processor"))

    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
}

firefly {
    includeDependencies = false
    layer = "firefly-test-layer"
}
