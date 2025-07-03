package me.santio.firefly.plugin.dependency.extension

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.reflect.TypeOf

interface DependencyExtension<T> {

    val type: TypeOf<T>
    val function: T

}

internal fun <T: Any> ExtensionContainer.addExtension(name: String, extension: DependencyExtension<T>) {
    this.add(extension.type, name, extension.function)
}