package me.santio.firefly.data

import java.time.temporal.ChronoUnit
import kotlin.math.floor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toKotlinDuration

val Int.ticks: Duration
    get() = (this * 50L).milliseconds

val Duration.inWholeTicks: Long
    get() = this.inWholeMilliseconds / 50

fun Duration.human(
    abbreviated: Boolean = false,
    relative: Boolean = false,
    minimum: ChronoUnit = ChronoUnit.SECONDS
): String {
    val segments = mutableListOf<String>()
    var remaining = this.absoluteValue
    val units = ChronoUnit.entries
        .sortedBy { -it.duration.toKotlinDuration() }
        .takeWhile { it.duration >= minimum.duration }

    fun format(value: Int, unit: ChronoUnit): String {
        val name = unit.name.lowercase()
        return if (abbreviated) "${value}${name[0]}" else "$value $name"
    }

    for (unit in units) {
        val kotlin = unit.duration.toKotlinDuration()
        if (kotlin > remaining) continue

        val value = floor(remaining / kotlin).toInt()
        remaining -= (kotlin * value)

        segments += format(value, unit)
        if (remaining <= Duration.ZERO) break
    }

    val postfix = segments.size > 1 && !abbreviated
    val timePart = segments.takeIf { it.isNotEmpty() }
        ?.filter { it.isNotBlank() }
        ?.joinToString(" ", postfix = " and ".takeIf { postfix } ?: "")
        ?: format(0, minimum)

    return if (relative) {
        return if (this > Duration.ZERO) "in $timePart"
        else "$timePart ago"
    } else timePart
}

fun now() = Timestamp.now()