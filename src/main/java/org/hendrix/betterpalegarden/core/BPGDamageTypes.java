package org.hendrix.betterpalegarden.core;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link DamageType Damage Types}
 */
public final class BPGDamageTypes {

    //#region Damage Types

    public static final RegistryKey<DamageType> THORN_BUSH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, IdentifierUtils.modIdentifier("thorn_bush"));

    //#endregion

    /**
     * Create a {@link DamageSource Damage Source}
     *
     * @param world The {@link World World reference}
     * @param key The {@link RegistryKey<DamageType> Damage Type Registry Key}
     * @return The {@link DamageSource Damage Source}
     */
    public static DamageSource of(final World world, final RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(key));
    }

}