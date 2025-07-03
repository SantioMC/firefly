package me.santio.firefly.config.toml

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Contextual
import kotlinx.serialization.serializer
import me.santio.firefly.config.Language
import java.io.InputStream
import kotlin.reflect.KType

class TomlLanguage(
    private val toml: Toml = Toml()
): Language {

    @Suppress("UNCHECKED_CAST")
    override fun <T : @Contextual Any> parse(stream: InputStream, type: KType): T? {
        return toml.decodeFromString(serializer(type), stream.bufferedReader().readText()) as T?
    }

}

@Suppress("unused")
val Language.Companion.Toml by lazy { TomlLanguage() }