dependencies {
    compileOnly(project(":config"))

    api(libs.kotlinx.serialization.json)
    api(libs.pkl)
}