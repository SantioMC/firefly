package me.santio.firefly.plugin.dependency

import me.santio.firefly.plugin.config.ConfigKind

open class FireflyDependencyHandler() {

    private val groupId = "me.santio.firefly"

    fun core(version: String = "+") = "$groupId:common:$version"
    fun processor(version: String = "+") = "$groupId:processor:$version"
    fun paper(version: String = "+") = "$groupId:paper:$version"
    fun game(version: String = "+") = "$groupId:game:$version"
    fun config(kind: ConfigKind, version: String = "+") = "$groupId:config-${kind.id}:$version"

}