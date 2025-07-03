package me.santio.firefly.paper.instance

import me.santio.firefly.dataDirectory
import me.santio.firefly.paper.sync
import net.kyori.adventure.util.TriState
import org.bukkit.*
import java.util.UUID.randomUUID
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.moveTo

object TemplateWorld {

    private val DefaultGamerules = mapOf(
        GameRule.DO_MOB_LOOT to false,
        GameRule.DO_INSOMNIA to false,
        GameRule.DO_MOB_SPAWNING to false,
        GameRule.DO_FIRE_TICK to false,
        GameRule.RANDOM_TICK_SPEED to 0,
        GameRule.SPAWN_RADIUS to 0,
        GameRule.SPAWN_CHUNK_RADIUS to 0,
        GameRule.ANNOUNCE_ADVANCEMENTS to false,
        GameRule.COMMAND_BLOCK_OUTPUT to false,
        GameRule.DISABLE_RAIDS to true,
        GameRule.DO_DAYLIGHT_CYCLE to false,
        GameRule.DO_ENTITY_DROPS to false,
        GameRule.DO_WEATHER_CYCLE to false,
        GameRule.DO_TILE_DROPS to false,
        GameRule.GLOBAL_SOUND_EVENTS to false,
    )

    suspend fun create() {
        sync {
            val template = dataDirectory.resolve("template")
            if (template.exists()) return@sync

            val uuid = randomUUID().toString()
            val world = Bukkit.getServer().createWorld(WorldCreator(uuid).generator(VoidGenerator).keepSpawnLoaded(TriState.FALSE))
                ?: error("Failed to create template world '$uuid', which is required by Firefly")


            world.difficulty = Difficulty.NORMAL
            world.isAutoSave = false
            world.spawnLocation = Location(world, 0.0, 100.0, 0.0)

            DefaultGamerules.forEach { (rule, value) ->
                world.setGameRule(rule as GameRule<Any>, value)
            }

            val path = world.worldFolder.toPath()
            Bukkit.getServer().unloadWorld(world, true)

            path.moveTo(template)

            template.resolve(".firefly").createFile()
            template.resolve("uid.dat").deleteIfExists()
            template.resolve("session.lock").deleteIfExists()
        }
    }

    fun removeDuplicates() {
        val worlds = Bukkit.getServer().worldContainer.listFiles {
            it.isDirectory && it.resolve(".firefly").exists()
        }

        worlds.forEach { it.deleteRecursively() }
    }

}