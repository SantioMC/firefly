package me.santio.firefly.game

import com.google.auto.service.AutoService
import me.santio.firefly.command.Parser
import me.santio.firefly.paper.command.Source

@AutoService(Parser::class)
object GameManager: Parser<Source, Game> {

    override suspend fun classType() = Game::class.java

    private val _games = mutableSetOf<Game>()
    val games: List<Game> get() = _games.toList()

    fun registerGame(game: Game) {
        _games.add(game)
    }

    override suspend fun resolve(id: String): Game? {
        return _games.find { it.id == id }
    }

    override suspend fun suggestions(): Iterable<String> {
        return _games.map { it.id }
    }

}