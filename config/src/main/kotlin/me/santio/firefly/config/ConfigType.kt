package me.santio.firefly.config

import java.io.InputStream
import kotlin.reflect.KType

interface Language {
    fun <T: Any> parse(stream: InputStream, type: KType): T?

    companion object
}