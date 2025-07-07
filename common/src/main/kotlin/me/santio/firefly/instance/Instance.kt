package me.santio.firefly.instance

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import me.santio.firefly.coroutine.Scoped
import me.santio.firefly.identity.Identifiable
import me.santio.firefly.player.FireflyPlayer

abstract class Instance<Container>(
    protected val container: Container
): Identifiable, Scoped {

    final override val prefix: String = "inst"
    final override lateinit var scope: CoroutineScope

    var fallback: Boolean = false

    abstract suspend fun teleport(player: FireflyPlayer)
    abstract fun contains(player: FireflyPlayer): Boolean
    abstract suspend fun loadSchematic(name: String)

    open suspend fun unload() {
        scope.cancel()
    }

    open suspend fun load() {
        scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    open suspend fun delete() {
        unload()
        InstanceManager.remove(this)
    }

}