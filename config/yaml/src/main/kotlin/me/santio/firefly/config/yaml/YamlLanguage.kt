package me.santio.firefly.config.yaml

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.Contextual
import kotlinx.serialization.serializer
import me.santio.firefly.config.Language
import java.io.InputStream
import kotlin.reflect.KType

class YamlLanguage(
    private val yaml: Yaml = Yaml()
): Language {

    @Suppress("UNCHECKED_CAST")
    override fun <T : @Contextual Any> parse(stream: InputStream, type: KType): T? {
        return yaml.decodeFromStream(serializer(type), stream) as T?
    }

}

@Suppress("unused")
val Language.Companion.Yaml by lazy { YamlLanguage() }
