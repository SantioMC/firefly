plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)

    id("me.santio.firefly")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":common"))

    ksp(project(":processor"))

    implementation(kotlin("stdlib"))
    implementation(libs.cloud.platform.paper)

    compileOnlyApi(libs.papermc)
    compileOnlyApi(libs.fawe.core)
    compileOnlyApi(libs.fawe.bukkit) { isTransitive = true }
}

firefly {
    includeDependencies = false
}