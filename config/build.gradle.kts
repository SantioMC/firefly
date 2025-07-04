plugins {
    alias(libs.plugins.ksp)
    id("me.santio.firefly")
}

dependencies {
    ksp(project(":processor"))
}

firefly {
    includeDependencies = false
}