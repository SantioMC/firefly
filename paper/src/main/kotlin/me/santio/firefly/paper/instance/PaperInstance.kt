package me.santio.firefly.paper.instance

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import io.papermc.paper.entity.TeleportFlag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.santio.firefly.dataDirectory
import me.santio.firefly.identity.generateId
import me.santio.firefly.instance.Instance
import me.santio.firefly.instance.InstanceManager
import me.santio.firefly.paper.player.PaperFireflyPlayer
import me.santio.firefly.paper.player.firefly
import me.santio.firefly.paper.plugin
import me.santio.firefly.paper.protection.ProtectionRules
import me.santio.firefly.paper.requirePlugin
import me.santio.firefly.paper.sync
import me.santio.firefly.player.FireflyPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.util.TriState
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.event.player.PlayerTeleportEvent
import java.io.File
import java.io.FileInputStream
import kotlin.io.path.*


class PaperInstance internal constructor(
    name: String = generateId()
): Instance<String>(name) {

    override val id: String = name
    internal var isVanilla: Boolean = false
    internal var protection: ProtectionRules? = null

    val world: World?
        get() = Bukkit.getWorld(container)

    val dataFolder: File = Bukkit.getServer().worldContainer.resolve(container)

    internal fun matches(world: World) = world.name == container

    @OptIn(ExperimentalPathApi::class)
    override suspend fun load() {
        super.load()
        val creator = WorldCreator.name(container)
            .generator(VoidGenerator)
            .keepSpawnLoaded(TriState.FALSE)

        if (dataFolder.exists()) {
            return sync { creator.createWorld() }
        }

        val template = dataDirectory.resolve("template").takeIf { it.exists() }
            ?: error("Failed to find generated template world")

        val path = plugin.server.worldContainer.toPath().resolve(container)
        if (path.exists()) error("Failed to create instance with pre-existing world folder '${path.absolutePathString()}'")

        template.copyToRecursively(path, followLinks = false)

        sync { creator.createWorld() }
    }

    override suspend fun unload() {
        super.unload()
        if (isVanilla) error("You can't unload vanilla worlds")

        val world = world ?: return
        val fallback = InstanceManager.fallback()

        world.players.forEach { player ->
            if (fallback != null) fallback.teleport(player.firefly)
            else player.kick(Component.text("No fallback instances were available", NamedTextColor.RED))
        }

        sync { Bukkit.getServer().unloadWorld(container, false) }
    }

    override suspend fun teleport(player: FireflyPlayer) {
        if (player !is PaperFireflyPlayer) return

        player.bukkit.teleportAsync(
            world?.spawnLocation ?: error("Can't teleport to instance '${container}', world isn't loaded"),
            PlayerTeleportEvent.TeleportCause.PLUGIN,
            TeleportFlag.EntityState.RETAIN_PASSENGERS,
        ).get()
    }

    override fun contains(player: FireflyPlayer): Boolean {
        if (player !is PaperFireflyPlayer) return false
        return player.bukkit.world.name == container
    }

    override suspend fun delete() {
        if (isVanilla) error("You can't delete vanilla worlds")

        super.delete()
        dataFolder.deleteRecursively()
    }

    override suspend fun loadSchematic(name: String) {
        requirePlugin("FastAsyncWorldEdit")

        val world = this.world?.let { BukkitAdapter.adapt(it) } ?: error("Can't load in schematic, world isn't loaded")
        val name = name.removeSuffix(".schem")

        val schematic = dataDirectory.resolve("schematics").resolve("$name.schem")
        if (schematic.notExists()) error("The provided schematic '$name' does not exist!")

        withContext(Dispatchers.IO) {
            val file = schematic.toFile()
            val format = ClipboardFormats.findByFile(file)
                ?: error("The provided schematic '$name' is in an unknown format!")

            val clipboard = format.getReader(FileInputStream(file)).use { it.read() }

            WorldEdit.getInstance().newEditSession(world).use { editSession ->
                val operation = ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .ignoreAirBlocks(true)
                    .copyEntities(true)
                    .copyBiomes(true)
                    .to(BlockVector3.at(0, 100, 0))
                    .build()

                Operations.complete(operation)
            }
        }
    }

    fun protect(rules: ProtectionRules.() -> Unit = {}) {
        protection = ProtectionRules().apply(rules)
    }

    fun unprotect() {
        protection = null
    }
}