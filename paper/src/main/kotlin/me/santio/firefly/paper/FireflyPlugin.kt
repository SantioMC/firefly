package me.santio.firefly.paper

import io.leangen.geantyref.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import me.santio.firefly.Color
import me.santio.firefly.command.FireflyCommand
import me.santio.firefly.command.Parser
import me.santio.firefly.coroutine.Scoped
import me.santio.firefly.dataDirectory
import me.santio.firefly.hook.ShutdownHook
import me.santio.firefly.instance.InstanceManager
import me.santio.firefly.loader.KotlinServiceLoader
import me.santio.firefly.paper.command.FireflySenderMapper
import me.santio.firefly.paper.command.Source
import me.santio.firefly.paper.instance.PaperInstance
import me.santio.firefly.paper.instance.TemplateWorld
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import org.incendo.cloud.paper.PaperCommandManager
import kotlin.io.path.createDirectories

abstract class FireflyPlugin : JavaPlugin(), Scoped {

    private val shutdownHooks = mutableListOf<ShutdownHook>()
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val serviceLoader = KotlinServiceLoader(classLoader)

    val internalMiniMessage = MiniMessage.builder()
        .editTags {
            it.resolver(Placeholder.styling("primary", Color.rgb(0xf96f25).toTextColor()))
            it.resolver(Placeholder.styling("body", NamedTextColor.GRAY))
            it.resolver(Placeholder.parsed("prefix", "<primary>\uD83D\uDD25 Firefly</primary> <dark_gray>|</dark_gray> "))
        }
        .build()

    open fun disable() {}
    open suspend fun enable() {}

    @Suppress("UnstableApiUsage")
    final override fun onEnable() {
        val commandManager = PaperCommandManager.builder(FireflySenderMapper)
            .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
            .buildOnEnable(this@FireflyPlugin)

        val annotationParser = AnnotationParser(commandManager, Source::class.java)
            .installCoroutineSupport()

        runBlocking {
            serviceLoader.load<Parser<Source, *>>().forEach { parser ->
                commandManager.parserRegistry().registerParserSupplier(TypeToken.get(parser.classType())) { parser }
            }

            serviceLoader.load<FireflyCommand>().forEach { command ->
                annotationParser.parse(command)
            }

            TemplateWorld.removeDuplicates()
            TemplateWorld.create()
        }

        launch {
            dataDirectory.createDirectories()
            dataDirectory.resolve("schematics").createDirectories()

            createVanillaInstances()

            serviceLoader.load<ShutdownHook>().forEach { shutdownHooks.add(it) }
            serviceLoader.load<Listener>().forEach { listener ->
                server.pluginManager.registerEvents(listener, this@FireflyPlugin)
            }

            enable()
        }
    }

    final override fun onDisable() {
        shutdownHooks.forEach { it.shutdown() }

        InstanceManager.instances.filterIsInstance<PaperInstance>()
            .filter { !it.isVanilla }
            .forEach {
                runBlocking { it.delete() }
            }

        disable()
    }

    private fun createVanillaInstances() {
        server.worlds.forEachIndexed { index, world ->
            InstanceManager.register(PaperInstance(world.name).apply {
                fallback = (index == 0)
                isVanilla = true
            })
        }
    }
}
