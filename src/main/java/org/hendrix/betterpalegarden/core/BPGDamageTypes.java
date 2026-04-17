package org.hendrix.betterpalegarden.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden} {@link DamageType Damage Types}
 */
public final class BPGDamageTypes {

    //#region Damage Types

    public static final ResourceKey<DamageType> THORN_BUSH = ResourceKey.create(Registries.DAMAGE_TYPE, IdentifierUtils.modded("thorn_bush"));

    //#endregion

}