package me.santio.firefly.data

import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class Timestamp private constructor(
    private val instant: Instant
): Comparable<Timestamp> {

    operator fun plus(duration: Duration) = of(instant.plusMillis(duration.inWholeMilliseconds))
    operator fun minus(duration: Duration) = of(instant.minusMillis(duration.inWholeMilliseconds))
    operator fun minus(other: Timestamp) = (instant.toEpochMilli() - other.instant.toEpochMilli()).milliseconds

    override fun compareTo(other: Timestamp): Int {
        return instant.compareTo(other.instant)
    }

    override fun equals(other: Any?) = instant == other
    override fun hashCode() = instant.hashCode()
    override fun toString() = instant.toString()

    fun relative(): Duration = this - now()

    @Suppress("unused")
    companion object {
        fun now() = Timestamp(Instant.now())
        fun of(instant: Instant) = Timestamp(instant)
        fun epochSeconds(epoch: Long) = Timestamp(Instant.ofEpochSecond(epoch))
        fun epochMilliseconds(epoch: Long) = Timestamp(Instant.ofEpochMilli(epoch))
    }

}