package me.santio.firefly.game.team

import net.kyori.adventure.text.Component

interface Team {
    val id: String
    val prefix: Component
}