package org.hendrix.betterpalegarden.utils;

import net.minecraft.util.Identifier;
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

}