package me.santio.firefly.data

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams

fun buildByteArray(block: ByteArrayDataOutput.() -> Unit): ByteArray {
    return ByteStreams.newDataOutput().apply(block).toByteArray()
}