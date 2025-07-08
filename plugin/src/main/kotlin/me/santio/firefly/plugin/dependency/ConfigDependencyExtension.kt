package me.santio.firefly.plugin.dependency

import me.santio.firefly.plugin.config.ConfigKind
import me.santio.firefly.plugin.config.FireflyConfiguration
import me.santio.firefly.plugin.extension.DependencyExtension
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.reflect.TypeOf

class ConfigDependencyExtension(
    private val handler: DependencyHandler,
    private val config: FireflyConfiguration,
): DependencyExtension<(ConfigKind) -> ExternalModuleDependency> {

    override val type = object : TypeOf<(ConfigKind) -> ExternalModuleDependency>() {}
    override val function: (ConfigKind) -> ExternalModuleDependency = ::handle

    fun handle(kind: ConfigKind): ExternalModuleDependency {
        val dependencyHandler = handler.extensions.findByType(FireflyDependencyHandler::class.java)!!
        return handler.add("implementation", dependencyHandler.config(kind, config.fireflyVersion)) as ExternalModuleDependency
    }

}