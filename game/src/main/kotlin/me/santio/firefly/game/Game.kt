package me.santio.firefly.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.santio.firefly.coroutine.Scoped
import me.santio.firefly.identity.Identifiable
import me.santio.firefly.identity.createId
import me.santio.firefly.paper.player.PaperFireflyPlayer
import me.santio.firefly.player.FireflyPlayer

abstract class Game(
    val name: String
): Identifiable, Scoped {
    override val prefix: String = "game"
    override val id: String = createId()

    override val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    var state: State = State.Stopped; private set
    val players = mutableListOf<PaperFireflyPlayer>()

    open suspend fun add(player: PaperFireflyPlayer) = players.add(player)

    open suspend fun remove(player: FireflyPlayer) {
        players.remove(player)

        if (players.isEmpty() && state == State.Running) {
            stop()
        }
    }

    fun start() {
        check(state == State.Stopped) { "The game is currently not stopped, you can't start it right now." }

        state = State.Running
        launch { doStart() }
    }

    suspend fun stop() {
        if (state == State.Stopped) return // Already at desired state

        state = State.Stopped
        players.forEach { reset(it) }
        launch { doEnd() }
    }

    suspend fun reset() {
        state = State.Stopped
        players.forEach { reset(it) }
        players.clear()
    }

    protected open suspend fun reset(player: FireflyPlayer) {}
    protected open suspend fun doStart() {}
    protected open suspend fun doEnd() {}

    enum class State {
        Running,
        Stopped
    }

}