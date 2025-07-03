package me.santio.firefly.identity

interface Identifiable {
    val prefix: String
    val id: String?
}