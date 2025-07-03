package me.santio.firefly.config.hocon

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.serializer
import me.santio.firefly.config.Language
import java.io.InputStream
import kotlin.reflect.KType

@OptIn(ExperimentalSerializationApi::class)
class HoconLanguage(
    private val hocon: Hocon = Hocon {}
): Language {

    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class)
    override fun <T : @Contextual Any> parse(stream: InputStream, type: KType): T? {
        val config = ConfigFactory.parseReader(stream.reader())
        return hocon.decodeFromConfig(serializer(type), config) as T?
    }

}

@OptIn(ExperimentalSerializationApi::class)
@Suppress("unused")
val Language.Companion.Hocon by lazy { HoconLanguage() }
