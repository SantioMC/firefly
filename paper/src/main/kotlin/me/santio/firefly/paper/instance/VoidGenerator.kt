package me.santio.firefly.paper.instance

import org.bukkit.block.Biome
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo

object VoidGenerator: ChunkGenerator() {
    override fun getDefaultBiomeProvider(worldInfo: WorldInfo): BiomeProvider? {
        return SingleBiomeProvider(Biome.PLAINS)
    }
}