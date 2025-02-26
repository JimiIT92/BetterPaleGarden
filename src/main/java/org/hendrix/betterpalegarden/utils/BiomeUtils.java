package org.hendrix.betterpalegarden.utils;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Optional;

/**
 * Utility methods for {@link Biome Biomes}
 */
public final class BiomeUtils {

    /**
     * Check if the provided {@link BlockPos Block Pos} is inside the {@link BiomeKeys#PALE_GARDEN Pale Garden Biome}
     *
     * @param world The {@link World World reference}
     * @param pos The {@link BlockPos Block Pos to check}
     * @return {@link Boolean True if the Block Pos is inside the Pale Garden}
     */
    public static boolean isInPaleGarden(final World world, final BlockPos pos) {
        final Optional<RegistryKey<Biome>> biomeKey = world.getBiome(pos).getKey();
        return biomeKey.isPresent() && biomeKey.get().equals(BiomeKeys.PALE_GARDEN);
    }

}