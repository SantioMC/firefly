package me.santio.firefly.plugin.config

import me.santio.firefly.plugin.BuildConfig
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

open class FireflyConfiguration {
    @Input @Optional var layer: String? = null
    @Input @Optional var fireflyVersion: String = BuildConfig.VERSION
    @Input @Optional var includeDependencies: Boolean = true
    @Input @Optional var configureShadow: Boolean = true
}