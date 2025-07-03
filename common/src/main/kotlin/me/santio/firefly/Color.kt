package me.santio.firefly

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("unused")
class Color private constructor(
    val red: Int,
    val green: Int,
    val blue: Int,
) {

    val argb: Int get() = (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
    val hex: String get() = String.format("#%06X", argb and 0xFFFFFF)

    fun darken(percentage: Int): Color {
        require(percentage in 0..100) { "Percentage must be between 0 and 255" }
        val factor = 1 - (percentage / 100.0)

        return Color(
            (red * factor).roundToInt().coerceIn(0, 255),
            (green * factor).roundToInt().coerceIn(0, 255),
            (blue * factor).roundToInt().coerceIn(0, 255)
        )
    }

    fun lighten(percentage: Int): Color {
        require(percentage in 0..100) { "Percentage must be between 0 and 255" }
        val factor = percentage / 100.0

        return Color(
            (red + (255 - red) * factor).roundToInt().coerceIn(0, 255),
            (green + (255 - green) * factor).roundToInt().coerceIn(0, 255),
            (blue + (255 - blue) * factor).roundToInt().coerceIn(0, 255)
        )
    }

    val hsl: HSL
        get() {
            val r = red / 255f
            val g = green / 255f
            val b = blue / 255f

            val max = max(r, max(g, b))
            val min = min(r, min(g, b))

            val h: Float
            val s: Float
            val l = (max + min) / 2f

            if (max == min) {
                h = 0f
                s = 0f
            } else {
                val d = max - min
                s = if (l > 0.5f) d / (2f - max - min) else d / (max + min)

                h = when (max) {
                    r -> (g - b) / d + (if (g < b) 6f else 0f)
                    g -> (b - r) / d + 2f
                    else -> (r - g) / d + 4f
                } / 6f
            }

            return HSL(h * 360f, s, l)
        }

    fun toJavaColor() = java.awt.Color(red, green, blue)

    class HSL internal constructor(val hue: Float, val saturation: Float, val lightness: Float) {
        fun toColor(): Color {
            if (saturation == 0f) {
                val gray = (lightness * 255).roundToInt()
                return Color(gray, gray, gray)
            }

            val c = (1 - abs(2 * lightness - 1)) * saturation
            val x = c * (1 - abs((hue / 60f) % 2 - 1))
            val m = lightness - c / 2

            val r: Float
            val g: Float
            val b: Float

            when (hue) {
                in 0f..<60f -> {
                    r = c
                    g = x
                    b = 0f
                }
                in 60f..<120f -> {
                    r = x
                    g = c
                    b = 0f
                }
                in 120f..<180f -> {
                    r = 0f
                    g = c
                    b = x
                }
                in 180f..<240f -> {
                    r = 0f
                    g = x
                    b = c
                }
                in 240f..<300f -> {
                    r = x
                    g = 0f
                    b = c
                }
                else -> {
                    r = c
                    g = 0f
                    b = x
                }
            }

            val red = ((r + m) * 255).roundToInt()
            val green = ((g + m) * 255).roundToInt()
            val blue = ((b + m) * 255).roundToInt()

            return Color(red, green, blue)
        }

        override fun toString(): String {
            return "Color.HSL(hue=$hue, saturation=$saturation, lightness=$lightness)"
        }
    }

    override fun toString(): String {
        return "Color(red=$red, green=$green, blue=$blue)"
    }

    companion object {
        fun rgb(red: Int, green: Int, blue: Int) = Color(red, green, blue)

        fun hsl(hue: Number, saturation: Number, lightness: Number)
            = HSL(hue.toFloat(), saturation.toFloat(), lightness.toFloat()).toColor()

        fun rgb(rgb: Int): Color {
            val red = (rgb shr 16) and 0xFF
            val green = (rgb shr 8) and 0xFF
            val blue = rgb and 0xFF

            return Color(red, green, blue)
        }

        fun hex(hex: String): Color {
            val hex = hex.removePrefix("#")
            require(hex.length == 6) { "Hex string must be 6 or 8 characters long (excluding '#')" }
            return rgb(hex.toInt(16))
        }
    }

}