package org.hendrix.betterpalegarden;

import net.fabricmc.api.ModInitializer;
import org.hendrix.betterpalegarden.core.BPGBlocks;
import org.hendrix.betterpalegarden.core.BPGItems;

/**
 * Hendrix's Better Pale Garden.<br/>
 * Boost the Pale Garden with white pumpkins,
 * new resin blocks and a new structure!
 */
public final class BetterPaleGarden implements ModInitializer {

    /**
     * The {@link String Mod ID}
     */
    public static final String MOD_ID = "betterpalegarden";

    /**
     * Initialize the mod
     */
    @Override
    public void onInitialize() {
        BPGItems.register();
        BPGBlocks.register();
    }

}