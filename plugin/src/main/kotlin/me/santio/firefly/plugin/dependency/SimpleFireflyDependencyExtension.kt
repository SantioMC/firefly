package me.santio.firefly.plugin.dependency

import me.santio.firefly.plugin.extension.DependencyExtension
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.reflect.TypeOf

class SimpleFireflyDependencyExtension(
    private val handler: DependencyHandler,
    private val dependency: String,
    private val configuration: String = "implementation",
): DependencyExtension<() -> ExternalModuleDependency> {

    override val type = object : TypeOf<() -> ExternalModuleDependency>() {}
    override val function: () -> ExternalModuleDependency = ::handle

    fun handle(): ExternalModuleDependency {
        return handler.add(configuration, dependency) as ExternalModuleDependency
    }

}