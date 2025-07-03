package me.santio.firefly.game.command

import com.google.auto.service.AutoService
import me.santio.firefly.command.FireflyCommand
import me.santio.firefly.game.Game
import me.santio.firefly.game.GameManager
import me.santio.firefly.paper.command.Source
import me.santio.firefly.paper.message.MessageKind
import me.santio.firefly.paper.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

@AutoService(FireflyCommand::class)
@Permission("firefly.admin.game")
object GameCommand: FireflyCommand {

    private fun createLine(game: Game): Component {
        val controls = when(game.state) {
            Game.State.Stopped ->
                """
                <hover:show_text:'<green>Click to start game'><click:run_command:'/f game start ${game.id}'><green>⏺
                <hover:show_text:'<gold>Click to reset game data'><click:run_command:'/f game reset ${game.id}'><gold>🗘
                """.trimIndent().replace("\n", " ")
            Game.State.Running ->
                """
                <hover:show_text:'<red>Click to force end game'><click:run_command:'/f game end ${game.id}'><red>🗙
                """.trimIndent().replace("\n", " ")
        }

        val hover = """
            <body>State: <primary><state>
            <body>Players: <primary><players>
            <body>Identifier: <primary><id>
        """.trimIndent()

        return plugin.internalMiniMessage.deserialize(
            "<hover:show_text:'<hover>'><primary><name><body> - <players> players <dark_gray>| <controls>",
            Placeholder.unparsed("name", game.name),
            Placeholder.unparsed("players", game.players.size.toString()),
            Placeholder.unparsed("id", game.id),
            Placeholder.unparsed("state", game.state.name),
            Placeholder.parsed("controls", controls),
            Placeholder.parsed("hover", hover),
        )
    }

    @Command("f game list")
    @Permission("firefly.admin.game.list")
    fun listGames(source: Source) {
        source.sender.sendMessage(plugin.internalMiniMessage.deserialize("<prefix><body>All available games:"))
        for (game in GameManager.games) {
            source.sender.sendMessage(createLine(game))
        }
    }

    @Command("f game join <game>")
    @Permission("firefly.admin.game.join")
    suspend fun joinGame(source: Source.Player, game: Game) {
        if (game.players.contains(source.firefly)) {
            return source.firefly.send(MessageKind.ERROR, "You are already in this game!")
        }

        game.add(source.firefly)
        source.firefly.send(MessageKind.SUCCESS, "You have been added to the game!")
    }

    @Command("f game leave <game>")
    @Permission("firefly.admin.game.leave")
    suspend fun leaveGame(source: Source.Player, game: Game) {
        if (!game.players.contains(source.firefly)) {
            return source.firefly.send(MessageKind.ERROR, "You are not in this game!")
        }

        game.remove(source.firefly)
        source.firefly.send(MessageKind.SUCCESS, "You have been removed from the game!")
    }

    @Command("f game reset <game>")
    @Permission("firefly.admin.game.reset")
    suspend fun resetGame(source: Source.Player, game: Game) {
        game.reset()
        source.firefly.send(MessageKind.SUCCESS, "You have reset the game successfully!")
    }

    @Command("f game start <game>")
    @Permission("firefly.admin.game.start")
    fun startGame(source: Source.Player, game: Game) {
        if (game.state != Game.State.Stopped) {
            return source.firefly.send(MessageKind.ERROR, "You can only start the game while it's stopped!")
        }

        game.start()
        source.firefly.send(MessageKind.SUCCESS, "The game has been successfully started")
    }

    @Command("f game end <game>")
    @Permission("firefly.admin.game.end")
    suspend fun endGame(source: Source.Player, game: Game) {
        if (game.state == Game.State.Stopped) {
            return source.firefly.send(MessageKind.ERROR, "You can't stop an already stopped game!")
        }

        game.stop()
        source.firefly.send(MessageKind.SUCCESS, "The game has been stopped!")
    }

}