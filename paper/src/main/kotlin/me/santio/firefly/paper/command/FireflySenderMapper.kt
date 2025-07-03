package me.santio.firefly.paper.command

import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.incendo.cloud.SenderMapper

@Suppress("UnstableApiUsage")
object FireflySenderMapper: SenderMapper<CommandSourceStack, Source> {
    override fun map(stack: CommandSourceStack): Source {
        if (stack.sender is ConsoleCommandSender) {
            return Source.Console(stack)
        }

        if (stack.sender is Player) {
            return Source.Player(stack)
        }

        if (stack.sender is Entity) {
            return Source.Entity(stack)
        }

        return Source.Generic(stack)
    }

    override fun reverse(mapped: Source): CommandSourceStack {
        return mapped.stack
    }
}