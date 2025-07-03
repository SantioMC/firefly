package me.santio.firefly.game

import me.santio.firefly.player.FireflyPlayer
import me.santio.firefly.game.state.PlayerGameState
import me.santio.firefly.game.team.Team

var FireflyPlayer.team: Team?
    get() = this.get<PlayerGameState>()?.team
    set(value) = this.update<PlayerGameState> {
        team = value
    }