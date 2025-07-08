package me.santio.firefly.plugin.dependency

import me.santio.firefly.plugin.config.FireflyConfiguration
import me.santio.firefly.plugin.extension.DependencyExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.reflect.TypeOf

class SimpleFireflyDependencyExtension(
    private val target: Project,
    private val config: FireflyConfiguration,
    private val dependency: (String) -> String,
    private val configuration: String = "implementation",
): DependencyExtension<() -> ExternalModuleDependency> {

    override val type = object : TypeOf<() -> ExternalModuleDependency>() {}
    override val function: () -> ExternalModuleDependency = ::handle

    fun handle(): ExternalModuleDependency {
        return target.dependencies.add(configuration, dependency(config.fireflyVersion)) as ExternalModuleDependency
    }

}