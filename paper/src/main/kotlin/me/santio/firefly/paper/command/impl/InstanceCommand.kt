package me.santio.firefly.paper.command.impl

import com.google.auto.service.AutoService
import me.santio.firefly.command.FireflyCommand
import me.santio.firefly.identity.createId
import me.santio.firefly.instance.InstanceManager
import me.santio.firefly.paper.command.Source
import me.santio.firefly.paper.instance.PaperInstance
import me.santio.firefly.paper.instance.createInstance
import me.santio.firefly.paper.message.MessageKind
import me.santio.firefly.paper.plugin
import me.santio.firefly.paper.sync
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.GameMode
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import kotlin.time.measureTimedValue

@Permission("firefly.admin.instance")
@AutoService(FireflyCommand::class)
object InstanceCommand: FireflyCommand {

    @Command("f instance create")
    @Permission("firefly.admin.instance.create")
    suspend fun create(source: Source.Player) {
        source.firefly.send("Creating new instance...")

        val time = measureTimedValue { createInstance() }

        source.firefly.send(
            MessageKind.SUCCESS,
            "Successfully created instance '${createId(time.value)}' and loaded in ${time.duration}"
        )
    }

    @Command("f instance list")
    @Permission("firefly.admin.instance.list")
    fun list(source: Source.Player) {
        source.sender.sendMessage(plugin.internalMiniMessage.deserialize("<prefix><body>All available instances:"))
        for (instance in InstanceManager.instances) {
            source.sender.sendMessage(
                plugin.internalMiniMessage.deserialize(
                    "<gray> - <primary><name>",
                    Placeholder.unparsed("name", createId(instance).asString)
                )
            )
        }
    }

    @Command("f instance tp <instance>")
    @Permission("firefly.admin.instance.teleport")
    suspend fun teleport(source: Source.Player, instance: PaperInstance) {
        instance.teleport(source.firefly)
        sync { source.sender.gameMode = GameMode.SPECTATOR }
        source.firefly.send(MessageKind.SUCCESS, "You were teleported to ${instance.id}")
    }

    @Command("f instance unload <instance>")
    @Permission("firefly.admin.instance.unload")
    suspend fun unload(source: Source.Player, instance: PaperInstance) {
        instance.unload()
        source.firefly.send(MessageKind.SUCCESS, "Successfully unloaded instance '${instance.id}")
    }

    @Command("f instance load <instance>")
    @Permission("firefly.admin.instance.load")
    suspend fun load(source: Source.Player, instance: PaperInstance) {
        instance.load()
        source.firefly.send(MessageKind.SUCCESS, "Successfully loaded instance '${instance.id}")
    }

    @Command("f instance delete <instance>")
    @Permission("firefly.admin.instance.delete")
    suspend fun delete(source: Source.Player, instance: PaperInstance) {
        instance.delete()
        source.firefly.send(MessageKind.SUCCESS, "Successfully deleted instance '${instance.id}")
    }

}