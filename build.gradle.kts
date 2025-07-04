plugins {
    `java-library`
    signing

    alias(libs.plugins.kotlin)
    alias(libs.plugins.publish)
}

allprojects {
    group = findProperty("group")!!
    version = findProperty("version")!!

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "signing")

    apply(plugin = rootProject.libs.plugins.kotlin.get().pluginId)
    apply(plugin = rootProject.libs.plugins.publish.get().pluginId)

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
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
            artifactId = project.createArtifactId(),
            version = project.version as String
        )
    }
}

fun Project.createArtifactId(): String {
    return if (parent != null && parent != rootProject) {
        "${parent!!.createArtifactId()}-${name}"
    } else name
}