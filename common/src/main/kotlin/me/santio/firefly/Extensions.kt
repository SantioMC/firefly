package me.santio.firefly

import org.objenesis.ObjenesisStd
import java.nio.file.Path
import kotlin.io.path.createDirectories

val dataDirectory: Path
    get() = Path.of("./firefly").apply { createDirectories() }

val objenesis: ObjenesisStd by lazy { ObjenesisStd() }