package org.hendrix.betterpalegarden.core;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.List;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link ConfiguredFeature Configured Features}
 */
public final class BPGConfiguredFeatures {

    //#region Configured Features

    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_WHITE_PUMPKIN = register("patch_white_pumpkin");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_THORN_BUSH = register("patch_thorn_bush");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_CHRYSANTHEMUM = register("patch_chrysanthemum");

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
        ConfiguredFeatures.register(
                featureRegisterable,
                PATCH_CHRYSANTHEMUM,
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(BPGBlocks.CHRYSANTHEMUM)))
        );
    }

    /**
     * Register a {@link ConfiguredFeature Configured Feature}
     *
     * @param name The {@link String Configured Feature Name}
     * @return The {@link RegistryKey<ConfiguredFeature> registered Configured Feature Registry Key}
     */
    private static RegistryKey<ConfiguredFeature<?, ?>> register(final String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, IdentifierUtils.modIdentifier(name));
    }

}