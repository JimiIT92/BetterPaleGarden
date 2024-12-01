package org.hendrix.betterpalegarden.core;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
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

    public static final RegistryKey<PlacedFeature> PATCH_WHITE_PUMPKIN = PlacedFeatures.of("patch_white_pumpkin");
    public static final RegistryKey<PlacedFeature> PATCH_THORN_BUSH = PlacedFeatures.of("patch_thorn_bush");

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
    }

    /**
     * Add the {@link PlacedFeature Placed Features} to the {@link Biome Biomes}
     */
    public static void addToBiomes() {
        final Predicate<BiomeSelectionContext> paleGardenBiomeSelector = BiomeSelectors.includeByKey(RegistryKey.of(RegistryKeys.BIOME, Identifier.of("pale_garden")));
        BiomeModifications.addFeature(
                paleGardenBiomeSelector,
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, IdentifierUtils.modIdentifier("patch_white_pumpkin"))
        );
        BiomeModifications.addFeature(
                paleGardenBiomeSelector,
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, IdentifierUtils.modIdentifier("patch_thorn_bush"))
        );
    }

}