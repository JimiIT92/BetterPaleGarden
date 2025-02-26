package org.hendrix.betterpalegarden.core;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.function.Predicate;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link PlacedFeature Placed Features}
 */
public final class BPGPlacedFeatures {

    //#region Placed Features

    public static final RegistryKey<PlacedFeature> PATCH_WHITE_PUMPKIN = register("patch_white_pumpkin");
    public static final RegistryKey<PlacedFeature> PATCH_THORN_BUSH = register("patch_thorn_bush");
    public static final RegistryKey<PlacedFeature> PATCH_CHRYSANTHEMUM = register("patch_chrysanthemum");

    //#endregion

    /**
     * Register all {@link PlacedFeature Placed Features}
     *
     * @param featureRegisterable The {@link Registerable<PlacedFeature> Placed Feature registerable}
     */
    public static void bootstrap(final Registerable<PlacedFeature> featureRegisterable) {
        final RegistryEntryLookup<ConfiguredFeature<?, ?>> registryEntryLookup = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        PlacedFeatures.register(
                featureRegisterable,
                PATCH_WHITE_PUMPKIN,
                registryEntryLookup.getOrThrow(BPGConfiguredFeatures.PATCH_WHITE_PUMPKIN),
                RarityFilterPlacementModifier.of(5),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
        PlacedFeatures.register(
                featureRegisterable,
                PATCH_THORN_BUSH,
                registryEntryLookup.getOrThrow(BPGConfiguredFeatures.PATCH_THORN_BUSH),
                RarityFilterPlacementModifier.of(5),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
        PlacedFeatures.register(
                featureRegisterable,
                PATCH_CHRYSANTHEMUM,
                registryEntryLookup.getOrThrow(BPGConfiguredFeatures.PATCH_CHRYSANTHEMUM),
                RarityFilterPlacementModifier.of(3),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
    }

    /**
     * Register a {@link PlacedFeature Placed Feature}
     *
     * @param name The {@link String Placed Feature Name}
     * @return The {@link RegistryKey<PlacedFeature> registered Placed Feature Registry Key}
     */
    private static RegistryKey<PlacedFeature> register(final String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, IdentifierUtils.modIdentifier(name));
    }

    /**
     * Add the {@link PlacedFeature Placed Features} to the {@link Biome Biomes}
     */
    public static void addToBiomes() {
        final Predicate<BiomeSelectionContext> paleGardenBiomeSelector = BiomeSelectors.includeByKey(BiomeKeys.PALE_GARDEN);
        BiomeModifications.addFeature(
                paleGardenBiomeSelector,
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, PATCH_WHITE_PUMPKIN.getValue())
        );
        BiomeModifications.addFeature(
                paleGardenBiomeSelector,
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, PATCH_THORN_BUSH.getValue())
        );
        BiomeModifications.addFeature(
                paleGardenBiomeSelector,
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, PATCH_CHRYSANTHEMUM.getValue())
        );
    }

}