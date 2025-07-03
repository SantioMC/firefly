package me.santio.firefly.paper.player

import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import java.lang.String.format

val Player.firefly: PaperFireflyPlayer
    get() = FireflyPlayerManager.identified(uniqueId) ?: error("The player '${name}' doesn't have a FireflyPlayer, are they online?")

fun Player.attribute(attribute: Attribute): Double {
    val attribute = getAttribute(attribute) ?: run {
        registerAttribute(attribute)
        getAttribute(attribute)
    } ?: error("Failed to add attribute '$attribute' to player")

    return attribute.value
}

fun Player.tablistPriority(priority: Int?) {
    val scoreboard = Bukkit.getServer().scoreboardManager.mainScoreboard

    scoreboard.teams
        .filter { it.name.startsWith("z-sort-") }
        .forEach {
            it.removePlayer(this)
            if (it.entries.isEmpty()) it.unregister()
        }

    if (priority == null) return

    val name = format("z-sort-%04d", priority)
    val team = scoreboard.getTeam(name) ?: scoreboard.registerNewTeam(name)

    team.addPlayer(this)
}