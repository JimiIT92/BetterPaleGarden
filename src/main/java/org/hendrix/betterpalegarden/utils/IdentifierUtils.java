package org.hendrix.betterpalegarden.utils;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.hendrix.betterpalegarden.BetterPaleGarden;

/**
 * Utility methods for {@link Identifier Identifier}
 */
public final class IdentifierUtils {

    /**
     * Get a {@link Identifier modded Identifier}
     *
     * @param name The {@link String resource name}
     * @return The {@link Identifier modded Identifier}
     */
    public static Identifier modIdentifier(final String name) {
        return Identifier.of(BetterPaleGarden.MOD_ID, name);
    }

    /**
     * Get the {@link RegistryKey<Biome> Pale Garden Registry Key}
     *
     * @return The {@link RegistryKey<Biome> Pale Garden Registry Key}
     */
    public static RegistryKey<Biome> paleGardenRegistryKey() {
        return RegistryKey.of(RegistryKeys.BIOME, Identifier.of("pale_garden"));
    }

}