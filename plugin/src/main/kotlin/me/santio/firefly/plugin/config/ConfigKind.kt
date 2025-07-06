package me.santio.firefly.plugin.config


enum class ConfigKind {
    Hocon,
    Json,
    Pkl,
    Toml,
    Yaml,
    ;

    val id: String
        get() = name.lowercase()

}