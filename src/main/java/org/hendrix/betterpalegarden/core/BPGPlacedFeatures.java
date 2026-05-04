package org.hendrix.betterpalegarden.core;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden} {@link PlacedFeature Placed Features}
 */
public final class BPGPlacedFeatures {

    /**
     * Add a {@link PlacedFeature} to the Pale Garden Biome
     *
     * @param name The {@link PlacedFeature} name
     */
    private static void addPlacedFeatureToPaleGarden(final String name) {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.PALE_GARDEN),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, IdentifierUtils.modded(name))
        );
    }

    /**
     * Register all {@link PlacedFeature Placed Features}
     */
    public static void register() {
        addPlacedFeatureToPaleGarden("patch_white_pumpkin");
        addPlacedFeatureToPaleGarden("patch_thorn_bush");
        addPlacedFeatureToPaleGarden("patch_chrysanthemum");
    }

}