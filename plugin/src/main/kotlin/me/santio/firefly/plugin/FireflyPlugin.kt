package me.santio.firefly.plugin

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import me.santio.firefly.plugin.config.FireflyConfiguration
import me.santio.firefly.plugin.dependency.ConfigDependencyExtension
import me.santio.firefly.plugin.dependency.FireflyDependencyHandler
import me.santio.firefly.plugin.dependency.SimpleFireflyDependencyExtension
import me.santio.firefly.plugin.extension.addExtension
import me.santio.firefly.plugin.extension.suffix
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.net.URI

@Suppress("unused")
class FireflyPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        val configuration = target.extensions.create("firefly", FireflyConfiguration::class.java)

        target.dependencies.extensions.create("firefly", FireflyDependencyHandler::class.java)

        // Default repositories required for Firefly to work
        target.repositories.mavenCentral()

        target.repositories.maven {
            // Required for PaperMC
            it.url = URI("https://repo.papermc.io/repository/maven-public/")
            it.name = "papermc"
        }

        createDependencyExtensions(target, configuration)
        target.afterEvaluate {
            if (configuration.includeDependencies) {
                target.repositories.maven {
                    // Automatically add the repository so we can add in dependencies
                    it.url = URI("https://repo.santio.me/repository/public/")
                    it.name = "firefly"
                }

                handleDependencies(target, configuration)
            }

            if (configuration.configureShadow) configureShadowJar(target, configuration.layer)
        }

        target.plugins.apply("java-library")
        target.plugins.apply("org.jetbrains.kotlin.jvm")
        target.plugins.apply("com.google.devtools.ksp")

        target.plugins.withId("org.jetbrains.kotlin.jvm") {
            configureKotlin(target)
        }
    }

    private fun handleDependencies(target: Project, configuration: FireflyConfiguration) {
        val dependencyHandler = target.dependencies.extensions
            .findByType(FireflyDependencyHandler::class.java)!!

        target.dependencies.add("implementation", dependencyHandler.core(configuration.fireflyVersion))
        target.dependencies.add("ksp", dependencyHandler.processor(configuration.fireflyVersion))
    }

    private fun createDependencyExtensions(target: Project, configuration: FireflyConfiguration) {
        val dependencyHandler = target.dependencies.extensions
            .findByType(FireflyDependencyHandler::class.java)!!

        target.dependencies.extensions.addExtension("game", SimpleFireflyDependencyExtension(
            target.dependencies,
            dependencyHandler.game(configuration.fireflyVersion),
        ))

        target.dependencies.extensions.addExtension("paper", SimpleFireflyDependencyExtension(
            target.dependencies,
            dependencyHandler.paper(configuration.fireflyVersion),
        ))

        target.dependencies.extensions.addExtension(
            "config",
            ConfigDependencyExtension(target.dependencies, configuration.fireflyVersion)
        )
    }

    private fun configureKotlin(target: Project) {
        val kotlinCompile = target.tasks.getByName("compileKotlin") as org.jetbrains.kotlin.gradle.tasks.KotlinCompile
        kotlinCompile.compilerOptions.freeCompilerArgs.add("-java-parameters")
    }

    private fun configureShadowJar(target: Project, layer: String?) {
        target.pluginManager.apply("com.gradleup.shadow")

        val layer = layer?.suffix("-layer")?.suffix(".jar")
        val build = target.tasks.getByName("build")

        val shadowJar = target.tasks.getByName("shadowJar") as ShadowJar
        shadowJar.mergeServiceFiles()
        if (layer != null) shadowJar.archiveFileName.set(layer)

        build.dependsOn(shadowJar)
    }

}