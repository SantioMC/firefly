package me.santio.firefly.paper.protection

import me.santio.firefly.state.State

data class PlayerProtectionState(
    var bypass: Boolean = false,
): State