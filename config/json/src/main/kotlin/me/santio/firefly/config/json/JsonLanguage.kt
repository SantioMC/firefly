package me.santio.firefly.config.json

import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import me.santio.firefly.config.Language
import java.io.InputStream
import kotlin.reflect.KType


class JsonLanguage(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
): Language {

    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class)
    override fun <T : @Contextual Any> parse(stream: InputStream, type: KType): T? {
        return json.decodeFromStream(serializer(type), stream) as T?
    }

}

@Suppress("unused")
val Language.Companion.Json by lazy { JsonLanguage() }
