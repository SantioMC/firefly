package me.santio.firefly.plugin.dependency

open class FireflyDependencyHandler() {

    private val groupId = "me.santio.firefly"

    fun core() = "$groupId:common"
    fun processor() = "$groupId:processor"

}