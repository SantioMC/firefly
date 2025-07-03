package me.santio.firefly.paper.instance

import org.bukkit.block.Biome
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.WorldInfo

class SingleBiomeProvider(
    private val biome: Biome
): BiomeProvider() {
    override fun getBiome(
        worldInfo: WorldInfo,
        x: Int,
        y: Int,
        z: Int
    ): Biome {
        return biome
    }

    override fun getBiomes(worldInfo: WorldInfo): List<Biome> {
        return listOf(biome)
    }

}