package me.santio.firefly.loader

import java.lang.reflect.Modifier
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaConstructor

class KotlinServiceLoader(
    private val classLoader: ClassLoader
) {

    suspend fun <T: Any> load(clazz: Class<T>, file: String = "META-INF/services/${clazz.name}"): List<T> {
        val classes = ClassServiceLoader(classLoader).load(clazz, file)
            ?: emptyList()

        return classes.map {
            val kotlin = it.kotlin
            val isObject = it.fields.any { field ->
                Modifier.isStatic(field.modifiers) && field.name == "INSTANCE"
            }

            @Suppress("UNCHECKED_CAST")
            if (isObject) return@map it.getDeclaredField("INSTANCE").get(null) as T

            val constructor = kotlin.primaryConstructor?.takeIf { constructor ->
                constructor.parameters.isEmpty()
            }?.javaConstructor ?: it.getDeclaredConstructor() ?: error("Failed to find an empty constructor for ${clazz.name}")

            constructor.newInstance()
        }
    }

    suspend inline fun <reified T: Any> load(file: String = "META-INF/services/${T::class.java.name}") = load(T::class.java, file)

}