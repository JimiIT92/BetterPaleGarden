package org.hendrix.betterpalegarden.core;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.entity.WhitePumpkinSnowGolem;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden} {@link EntityType entity types}
 */
public final class BPGEntityTypes {

    //#region Entity Types

    public static final EntityType<WhitePumpkinSnowGolem> SNOW_GOLEM = register(
            "snow_golem",
            EntityType.Builder.of(WhitePumpkinSnowGolem::new, MobCategory.MISC)
                    .immuneTo(BlockTags.SNOW_GOLEM_IMMUNE_TO)
                    .sized(0.7F, 1.9F)
                    .eyeHeight(1.7F)
                    .clientTrackingRange(8)
    );

    //#endregion

    /**
     * Register an entity
     *
     * @param name The entity name
     * @param builder The entity builder
     * @return The registered entity
     * @param <T> The entity type
     */
    private static <T extends Entity> EntityType<T> register(final String name, final EntityType.Builder<T> builder) {
        final ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, IdentifierUtils.modded(name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    /**
     * Register all entities
     */
    public static void register() {
        FabricDefaultAttributeRegistry.register(SNOW_GOLEM, WhitePumpkinSnowGolem.createAttributes());
    }

}