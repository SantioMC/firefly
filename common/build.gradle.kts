plugins {
    alias(libs.plugins.kotlin)
    id("me.santio.firefly")
}

dependencies {
    api(kotlin("reflect"))

    compileOnlyApi(libs.autoservice)
    api(libs.kotlinx.coroutines)
    api(libs.objenesis)

    api(libs.bundles.cloud)
}

kotlin {
    jvmToolchain(21)
}

firefly {
    includeDependencies = false
}