package me.santio.firefly.plugin.extension

fun String.prefix(prefix: String): String {
    if (this.startsWith(prefix)) return this
    return "$prefix$this"
}

fun String.suffix(suffix: String): String {
    if (this.endsWith(suffix)) return this
    return "$this$suffix"
}