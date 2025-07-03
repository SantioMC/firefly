package me.santio.firefly.config.pkl

import kotlinx.serialization.Contextual
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import me.santio.firefly.config.Language
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.config.kotlin.to
import org.pkl.core.ModuleSource
import org.pkl.core.ValueRenderers
import java.io.InputStream
import java.io.StringWriter
import kotlin.reflect.KType

class PklLanguage(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
): Language {

    private val evaluator = ConfigEvaluator.preconfigured().forKotlin()

    @Suppress("UNCHECKED_CAST")
    override fun <T : @Contextual Any> parse(stream: InputStream, type: KType): T? {
        val writer = StringWriter()
        val renderer = ValueRenderers.json(
            writer,
            "  ",
            false
        )

        val config = evaluator.use {
            it.evaluate(ModuleSource.text(stream.bufferedReader().readText()))
        }

        renderer.renderDocument(config.to<Map<String, Any>>())
        return json.decodeFromString(serializer(type), writer.toString()) as T?
    }

}

@Suppress("unused")
val Language.Companion.Pkl by lazy { PklLanguage() }
