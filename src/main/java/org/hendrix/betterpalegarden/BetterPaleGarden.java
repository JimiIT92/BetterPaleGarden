package org.hendrix.betterpalegarden;

import net.fabricmc.api.ModInitializer;
import org.hendrix.betterpalegarden.core.BPGBlocks;
import org.hendrix.betterpalegarden.core.BPGEntities;
import org.hendrix.betterpalegarden.core.BPGItemGroups;

/**
 * Hendrix's Better Pale Garden
 * Boost the Pale Garden with White Pumpkins, new Resin Blocks and a new Structure!
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
        BPGItemGroups.register();
        BPGBlocks.register();
        BPGEntities.register();
    }

}