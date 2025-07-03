package me.santio.firefly.loader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClassServiceLoader(
    private val classLoader: ClassLoader = Thread.currentThread().contextClassLoader,
) {

    @Suppress("UNCHECKED_CAST")
    suspend fun <T: Any> load(clazz: Class<T>, file: String = "META-INF/services/${clazz.name}"): List<Class<out T>>? {
        return withContext(Dispatchers.IO) {
            val resources = classLoader.getResources(file)?.toList() ?: emptyList()
            if (resources.isEmpty()) return@withContext emptyList()

            return@withContext resources.toList()
                .flatMap { url ->
                    url.openStream().bufferedReader().use {
                        it.readText()
                    }.lines()
                }
                .filter { it.isNotBlank() }
                .map { Class.forName(it, false, classLoader) as Class<out T> }
        }
    }

    suspend inline fun <reified T: Any> load(file: String = "META-INF/services/${T::class.java.name}") = load(T::class.java, file)

}