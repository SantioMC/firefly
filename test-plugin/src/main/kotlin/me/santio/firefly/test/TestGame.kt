package me.santio.firefly.test

import me.santio.firefly.player.FireflyPlayer
import me.santio.firefly.game.Game
import me.santio.firefly.instance.Instance
import me.santio.firefly.paper.instance.createInstance
import me.santio.firefly.paper.player.tablistPriority
import net.kyori.adventure.text.format.NamedTextColor

object TestGame: Game("Test Game") {

    private var instance: Instance<*>? = null

    override suspend fun doStart() {
        instance = createInstance {
            loadSchematic("test")
            protect {
                canInteract = true
            }
        }

        players.forEach {
            it.bukkit.tablistPriority(1)
            it.bukkit.playerListName(it.bukkit.name().color(NamedTextColor.GRAY))

            instance!!.teleport(it)
        }
    }

    override suspend fun doEnd() {
        instance?.delete()
    }

    override suspend fun reset(player: FireflyPlayer) {
        player.reset()
    }

}