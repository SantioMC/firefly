package me.santio.firefly.identity

class Id<Entity: Identifiable> internal constructor(
    entity: Entity,
    val prefix: String = entity.prefix.trim(),
    identifier: String = entity.id ?: generateId()
) {
    val hasPrefix = prefix.isNotBlank()

    private val id = identifier.apply {
        if (hasPrefix) removePrefix("${prefix}_")
    }

    val asString = "${prefix}_${id}".takeIf { hasPrefix } ?: id

    override fun toString(): String {
        return asString
    }
}