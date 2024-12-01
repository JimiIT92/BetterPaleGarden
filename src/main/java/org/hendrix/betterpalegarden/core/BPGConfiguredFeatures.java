package org.hendrix.betterpalegarden.core;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.hendrix.betterpalegarden.BetterPaleGarden;

import java.util.List;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link ConfiguredFeature Configured Features}
 */
public final class BPGConfiguredFeatures {

    //#region Configured Features

    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_WHITE_PUMPKIN = ConfiguredFeatures.of("patch_white_pumpkin");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_THORN_BUSH = ConfiguredFeatures.of("patch_thorn_bush");

    //#endregion

    /**
     * Register all {@link ConfiguredFeature Configured Features}
     *
     * @param featureRegisterable The {@link Registerable<ConfiguredFeature> Configured Feature registerable}
     */
    public static void bootstrap(final Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        ConfiguredFeatures.register(
                featureRegisterable,
                PATCH_WHITE_PUMPKIN,
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(BPGBlocks.WHITE_PUMPKIN)), List.of(Blocks.GRASS_BLOCK))
        );
        ConfiguredFeatures.register(
                featureRegisterable,
                PATCH_THORN_BUSH,
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(BPGBlocks.THORN_BUSH)))
        );
    }

}