package me.santio.firefly.hook

@FunctionalInterface
interface ShutdownHook {
    fun shutdown()
}