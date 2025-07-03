package me.santio.firefly.player

import kotlinx.coroutines.*
import me.santio.firefly.identity.Identifiable
import me.santio.firefly.instance.Instance
import me.santio.firefly.instance.InstanceManager
import me.santio.firefly.player.states.PlayerCooldownState
import me.santio.firefly.state.StateContainer
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration

@Suppress("CanBeParameter")
abstract class FireflyPlayer(
    open val username: String,
    open val uniqueId: UUID,
): StateContainer(), Identifiable {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override val prefix: String = ""
    override val id: String = uniqueId.toString()

    val instance: Instance<*>
        get() = InstanceManager.instances.first { it.contains(this) }

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = scope.launch(context, start, block)

    abstract suspend fun reset(
        inventory: Boolean = true,
        enderChest: Boolean = true,
        health: Boolean = true,
        food: Boolean = true,
        effects: Boolean = true,
        gamemode: Boolean = true,
        tablist: Boolean = true,
    )

    fun isOnCooldown(id: String) = get<PlayerCooldownState>()?.onCooldown(id) ?: false

    fun setCooldown(id: String, cooldown: Duration) = getOrPut<PlayerCooldownState> { PlayerCooldownState() }
        .cooldown(id, cooldown)

    fun withCooldown(id: String, cooldown: Duration, block: () -> Unit) {
        if (isOnCooldown(id)) return
        block()
        setCooldown(id, cooldown)
    }

}