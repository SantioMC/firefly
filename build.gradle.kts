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