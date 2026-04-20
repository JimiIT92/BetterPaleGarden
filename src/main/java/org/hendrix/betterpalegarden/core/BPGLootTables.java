package org.hendrix.betterpalegarden.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden} {@link LootTable Loot Tables}
 */
public final class BPGLootTables {

    //#region Loot Tables

    public static final ResourceKey<LootTable> SNOW_GOLEM = ResourceKey.create(Registries.LOOT_TABLE, IdentifierUtils.modded("shearing/snow_golem"));

    //#endregion

    /**
     * Register all loot tables
     */
    public static void register() {

    }

}