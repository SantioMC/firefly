package me.santio.firefly.test

import kotlinx.serialization.Serializable
import me.santio.firefly.config.ConfigLoader
import me.santio.firefly.config.Language
import me.santio.firefly.config.pkl.Pkl
import me.santio.firefly.dataDirectory
import me.santio.firefly.game.GameManager
import me.santio.firefly.paper.FireflyPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import kotlin.io.path.createDirectories

class TestPlugin: FireflyPlugin() { // todo: make a different impl

    override suspend fun enable() {
        GameManager.registerGame(TestGame)

        val path = dataDirectory.resolve("config").createDirectories()
        val config = ConfigLoader.fromPath<Test>(path.resolve("config.pkl"), Language.Pkl)

        Bukkit.broadcast(Component.text(config.message))
    }

}

@Serializable
data class Test(
    val message: String
)