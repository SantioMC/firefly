package me.santio.firefly.paper.protection

import com.google.auto.service.AutoService
import me.santio.firefly.paper.instance.PaperInstance
import me.santio.firefly.paper.message.MessageKind
import me.santio.firefly.paper.player.firefly
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.time.Duration.Companion.seconds

@AutoService(Listener::class)
object WorldProtection: Listener {

    private fun isValid(player: Player): Boolean {
        return player.firefly.get<PlayerProtectionState>()?.bypass != true
    }

    private fun alert(player: Player) = player.firefly.withCooldown("protect.warning", 5.seconds) {
        player.firefly.send(MessageKind.ERROR, "You are not permitted to do this here!")
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val instance = event.player.firefly.instance as PaperInstance
        if (!isValid(event.player) || instance.protection?.canBreak != false) return
        event.isCancelled = true
        alert(event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val instance = event.player.firefly.instance as PaperInstance
        if (!isValid(event.player) || instance.protection?.canPlace != false) return
        event.isCancelled = true
        alert(event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onInteract(event: PlayerInteractEvent) {
        val instance = event.player.firefly.instance as PaperInstance
        if (!isValid(event.player) || instance.protection?.canInteract != false) return
        event.isCancelled = true
    }

}