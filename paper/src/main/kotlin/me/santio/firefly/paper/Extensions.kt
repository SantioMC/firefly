package me.santio.firefly.paper

import kotlinx.coroutines.withContext
import me.santio.firefly.Color
import me.santio.firefly.paper.coroutine.PaperServerDispatcher
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

val plugin: FireflyPlugin
    get() = JavaPlugin.getPlugin(FireflyPlugin::class.java)

fun Color.toTextColor() = TextColor.color(this.red, this.green, this.blue)

suspend inline fun <T> sync(crossinline block: () -> T): T {
    return if (plugin.server.isPrimaryThread) block()
    else withContext(PaperServerDispatcher) { block() }
}

fun requirePlugin(name: String) {
    if (!Bukkit.getServer().pluginManager.isPluginEnabled(name)) {
        error("This feature requires the plugin '$name', please install it")
    }
}