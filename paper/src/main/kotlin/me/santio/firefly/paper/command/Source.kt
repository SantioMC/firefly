package me.santio.firefly.paper.command

import io.papermc.paper.command.brigadier.CommandSourceStack
import me.santio.firefly.paper.player.PaperFireflyPlayer
import me.santio.firefly.paper.player.firefly
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Entity as BukkitEntity
import org.bukkit.entity.Player as BukkitPlayer

@Suppress("UnstableApiUsage", "unused")
sealed class Source(internal open val stack: CommandSourceStack) {

    abstract val sender: CommandSender

    class Console internal constructor(override val stack: CommandSourceStack): Source(stack) {
        override val sender: ConsoleCommandSender get() = stack.sender as ConsoleCommandSender
    }

    class Player internal constructor(override val stack: CommandSourceStack): Source(stack) {
        override val sender: BukkitPlayer get() = stack.sender as BukkitPlayer
        val firefly: PaperFireflyPlayer get() = sender.firefly
    }

    class Entity internal constructor(override val stack: CommandSourceStack): Source(stack) {
        override val sender: BukkitEntity get() = stack.sender as BukkitEntity
    }

    class Generic internal constructor(override val stack: CommandSourceStack): Source(stack) {
        override val sender: CommandSender get() = stack.sender
    }

}