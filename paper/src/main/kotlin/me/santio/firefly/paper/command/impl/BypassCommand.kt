package me.santio.firefly.paper.command.impl

import com.google.auto.service.AutoService
import me.santio.firefly.command.FireflyCommand
import me.santio.firefly.paper.command.Source
import me.santio.firefly.paper.message.MessageKind
import me.santio.firefly.paper.protection.PlayerProtectionState
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

@Permission("firefly.admin.bypass")
@AutoService(FireflyCommand::class)
object BypassCommand: FireflyCommand {

    @Command("f bypass")
    @Permission("firefly.admin.bypass")
    suspend fun bypass(source: Source.Player) {
        val isBypassing = source.firefly.get<PlayerProtectionState>()?.bypass ?: false

        if (isBypassing) {
            source.firefly.delete<PlayerProtectionState>()
            source.firefly.send(MessageKind.SUCCESS, "You are no longer bypassing world protection!")
        } else {
            source.firefly.update<PlayerProtectionState> { bypass = true }
            source.firefly.send(MessageKind.SUCCESS, "You are now bypassing world protection!")
        }
    }

}