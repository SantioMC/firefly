package me.santio.firefly.game.state

import me.santio.firefly.game.team.Team
import me.santio.firefly.state.State

data class PlayerGameState(
    var team: Team? = null,
): State
