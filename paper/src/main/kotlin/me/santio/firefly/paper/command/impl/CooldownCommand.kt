package me.santio.firefly.paper.command.impl

import com.google.auto.service.AutoService
import me.santio.firefly.command.FireflyCommand
import me.santio.firefly.data.human
import me.santio.firefly.paper.command.Source
import me.santio.firefly.paper.message.MessageKind
import me.santio.firefly.paper.plugin
import me.santio.firefly.player.states.PlayerCooldownState
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

@Permission("firefly.admin.cooldown")
@AutoService(FireflyCommand::class)
object CooldownCommand: FireflyCommand {

    @Command("f cooldown list")
    @Permission("firefly.admin.cooldown.list")
    fun list(source: Source.Player) {
        val state = source.firefly.get<PlayerCooldownState>()
        val cooldowns = state?.cooldowns
            ?.filter { state.onCooldown(it.key) }
            ?: emptyMap()

        if (cooldowns.isEmpty()) return source.firefly.send("You have no cooldowns")

        source.firefly.send("Your currently active cooldowns:")
        for ((id, timestamp) in cooldowns) {
            val duration = timestamp.relative().human()

            source.sender.sendMessage(plugin.internalMiniMessage.deserialize(
                " <gray> - <primary><id><gray>: <duration>",
                Placeholder.unparsed("id", id),
                Placeholder.unparsed("duration", duration)
            ))
        }
    }

    @Command("f cooldown reset")
    @Permission("firefly.admin.cooldown.reset")
    fun reset(source: Source.Player) {
        source.firefly.delete<PlayerCooldownState>()
        source.firefly.send(MessageKind.SUCCESS, "Successfully removed all of your existing cooldowns")
    }

}