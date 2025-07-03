package me.santio.firefly.player.states

import me.santio.firefly.data.Timestamp
import me.santio.firefly.data.now
import me.santio.firefly.state.State
import kotlin.time.Duration

data class PlayerCooldownState(
    val cooldowns: MutableMap<String, Timestamp> = mutableMapOf()
): State {

    fun onCooldown(id: String) = cooldowns[id]?.let { it >= now() } ?: false
    fun cooldown(id: String, length: Duration) {
        cooldowns[id] = now() + length
    }

}
