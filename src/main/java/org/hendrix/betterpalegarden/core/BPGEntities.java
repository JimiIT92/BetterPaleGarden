package org.hendrix.betterpalegarden.core;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.entity.WhitePumpkinSnowGolemEntity;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link EntityType Entity Types}
 */
public final class BPGEntities {

    //#region Entity Types

    public static final EntityType<WhitePumpkinSnowGolemEntity> SNOW_GOLEM = Registry.register(
            Registries.ENTITY_TYPE,
            IdentifierUtils.modIdentifier("snow_golem"),
            EntityType.Builder.create(WhitePumpkinSnowGolemEntity::new, SpawnGroup.MISC)
                    .allowSpawningInside(Blocks.POWDER_SNOW)
                    .dimensions(0.7F, 1.9F)
                    .eyeHeight(1.7F)
                    .maxTrackingRange(8)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, IdentifierUtils.modIdentifier("snow_golem")))
    );

    //#endregion

    /**
     * Register all {@link EntityType Entity Types}
     */
    public static void register() {
        FabricDefaultAttributeRegistry.register(SNOW_GOLEM, WhitePumpkinSnowGolemEntity.createSnowGolemAttributes());
    }

}