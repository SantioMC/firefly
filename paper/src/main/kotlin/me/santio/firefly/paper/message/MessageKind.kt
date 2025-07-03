package me.santio.firefly.paper.message

import me.santio.firefly.Color
import me.santio.firefly.paper.toTextColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class MessageKind(
    val prefix: Component? = null,
    val textColor: TextColor? = null,
) {

    INFO(FireflyPrefix, NamedTextColor.GRAY),
    SUCCESS(FireflyPrefix, Color.rgb(0x90db91).toTextColor()),
    WARNING("⚠", NamedTextColor.GOLD, NamedTextColor.YELLOW),
    ERROR("🗙", NamedTextColor.DARK_RED, NamedTextColor.RED),
    ;

    constructor(prefix: String? = null, symbolColor: TextColor? = null, textColor: TextColor? = null)
        : this(prefix?.let { Component.text(prefix, symbolColor) }, textColor)

    fun createComponent(message: String): Component {
        val text = Component.text(message, textColor)
        if (prefix == null) return text

        return prefix.appendSpace().append(text)
    }
}

private val FireflyPrefix = Component.text("🔥 Firefly ", Color.rgb(0xf96f25).toTextColor())
    .append(Component.text("|", NamedTextColor.GRAY))