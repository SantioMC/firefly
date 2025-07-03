package me.santio.firefly.instance

import me.santio.firefly.player.FireflyPlayer
import me.santio.firefly.identity.Identifiable

abstract class Instance<Container>(
    protected val container: Container
): Identifiable {

    final override val prefix: String = "inst"
    var fallback: Boolean = false

    abstract suspend fun load()
    abstract suspend fun unload()
    abstract suspend fun teleport(player: FireflyPlayer)
    abstract fun contains(player: FireflyPlayer): Boolean

    abstract suspend fun loadSchematic(name: String)

    open suspend fun delete() {
        unload()
        InstanceManager.remove(this)
    }

}