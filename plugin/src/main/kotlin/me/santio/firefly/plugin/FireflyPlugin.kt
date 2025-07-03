package me.santio.firefly.plugin

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import me.santio.firefly.plugin.config.FireflyConfiguration
import me.santio.firefly.plugin.dependency.FireflyDependencyHandler
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class FireflyPlugin: Plugin<Project> {


    override fun apply(target: Project) {
        val configuration = target.extensions.create("firefly", FireflyConfiguration::class.java)
        target.dependencies.extensions.create("firefly", FireflyDependencyHandler::class.java)

        target.afterEvaluate {
            if (configuration.includeDependencies) {
                val dependencyHandler = target.dependencies.extensions
                    .findByType(FireflyDependencyHandler::class.java)!!

                target.dependencies.add("implementation", dependencyHandler.core())
                target.dependencies.add("ksp", dependencyHandler.processor())
            }

            if (configuration.configureShadow) {
                target.pluginManager.apply("com.gradleup.shadow")
                configureShadowJar(target, configuration.layer)
            }
        }

        configureKotlin(target)
    }

    private fun configureKotlin(target: Project) {
        val kotlinCompile = target.tasks.getByName("compileKotlin") as KotlinCompile
        kotlinCompile.compilerOptions.freeCompilerArgs.add("-java-parameters")
    }

    private fun configureShadowJar(target: Project, layer: String?) {
        val build = target.tasks.getByName("build")

        val shadowJar = target.tasks.getByName("shadowJar") as ShadowJar
        shadowJar.mergeServiceFiles()
        if (layer != null) shadowJar.archiveFileName.set(layer.removeSuffix(".jar") + ".jar")

        build.dependsOn(shadowJar)
    }

}