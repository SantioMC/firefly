package me.santio.firefly.paper.player

import me.santio.firefly.player.FireflyPlayer
import me.santio.firefly.paper.message.MessageKind
import me.santio.firefly.paper.sync
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class PaperFireflyPlayer(
    val bukkit: Player
): FireflyPlayer(bukkit.name, bukkit.uniqueId) {

    fun send(message: String) = send(MessageKind.INFO, message)
    fun send(kind: MessageKind = MessageKind.INFO, message: String) {
        bukkit.sendMessage(kind.createComponent(message))
    }

    override suspend fun reset(
        inventory: Boolean,
        enderChest: Boolean,
        health: Boolean,
        food: Boolean,
        effects: Boolean,
        gamemode: Boolean,
        tablist: Boolean,
    ) = sync {
        if (inventory) bukkit.inventory.clear()
        if (enderChest) bukkit.enderChest.clear()
        if (health) bukkit.health = bukkit.attribute(Attribute.MAX_HEALTH)
        if (food) bukkit.foodLevel = 20
        if (effects) bukkit.activePotionEffects.forEach { bukkit.removePotionEffect(it.type) }
        if (gamemode) bukkit.gameMode = DefaultGamemode

        if (tablist) {
            bukkit.tablistPriority(null)
            bukkit.playerListName(null)
        }
    }

    companion object {
        @JvmStatic var DefaultGamemode: GameMode = GameMode.ADVENTURE
    }
}