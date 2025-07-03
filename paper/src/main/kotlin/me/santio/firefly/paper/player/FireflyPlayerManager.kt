package me.santio.firefly.paper.player

import com.google.auto.service.AutoService
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import me.santio.firefly.paper.plugin
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

@AutoService(Listener::class)
object FireflyPlayerManager: Listener {

    private val players = ConcurrentHashMap<UUID, PaperFireflyPlayer>()

    fun all() = players.toList()
    fun named(name: String) = players.values.firstOrNull { it.username == name }
    fun identified(uniqueId: UUID) = players[uniqueId]

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private fun onConnect(event: PlayerJoinEvent) {
        players[event.player.uniqueId] = PaperFireflyPlayer(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun onDisconnect(event: PlayerQuitEvent) = plugin.launch {
        identified(event.player.uniqueId)?.apply {
            scope.cancel()
        }

        delay(10.seconds)
        if (event.player.isOnline) return@launch

        players.remove(event.player.uniqueId)
    }

}