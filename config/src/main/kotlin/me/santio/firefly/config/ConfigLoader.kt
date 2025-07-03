package me.santio.firefly.config

import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.reflect.typeOf

object ConfigLoader {

    inline fun <reified T: Any> fromStream(stream: InputStream, language: Language): T {
        return language.parse(stream, typeOf<T>()) ?: error("Failed to load config from stream")
    }

    inline fun <reified T: Any> fromPath(path: Path, language: Language): T {
        return fromStream<T>(path.inputStream(), language)
    }

}