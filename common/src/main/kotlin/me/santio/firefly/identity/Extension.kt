package me.santio.firefly.identity

import java.security.SecureRandom
import kotlin.random.asKotlinRandom

private val secure = SecureRandom().asKotlinRandom()

fun generateId(
    length: Int = 16,
    alphabet: String = "abcdefghijklmnopqrstuvwxyz1234567890"
): String = (1..length).map { alphabet.random(secure) }.joinToString("")

fun Identifiable.createId(
    length: Int = 16,
    alphabet: String = "abcdefghijklmnopqrstuvwxyz1234567890"
) = "${prefix}_${generateId(length, alphabet)}"

fun <Entity: Identifiable> createId(entity: Entity): Id<Entity> = Id(entity)