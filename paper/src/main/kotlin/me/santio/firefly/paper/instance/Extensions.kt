package me.santio.firefly.paper.instance

import me.santio.firefly.instance.InstanceManager
import org.bukkit.World

suspend fun createInstance(block: suspend PaperInstance.() -> Unit = {}): PaperInstance {
    return PaperInstance().apply {
        load()
        block()
        InstanceManager.register(this)
    }
}

val World.instance: PaperInstance
    get() = InstanceManager.instances.filterIsInstance<PaperInstance>().first { it.matches(this) }