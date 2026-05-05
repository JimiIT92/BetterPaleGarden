package org.hendrix.betterpalegarden;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.hendrix.betterpalegarden.config.BPGConfig;
import org.hendrix.betterpalegarden.core.*;

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
     * The {@link BPGConfig Mod Configuration}
     */
    private static BPGConfig CONFIG;
    /**
     * The {@link Long maximum Fog Thickness}
     */
    public static final long MAX_FOG_THICKNESS = 128L;
    /**
     * The {@link Long default Fog Thickness}
     */
    public static final long DEFAULT_FOG_THICKNESS = 96L;

    /**
     * Initialize the mod
     */
    @Override
    public void onInitialize() {
        BPGItems.register();
        BPGBlocks.register();
        BPGLootTables.register();
        BPGCreativeModeTabs.register();
        BPGEntityTypes.register();
        BPGPlacedFeatures.register();
        if(isClothConfigInstalled()) {
            AutoConfig.register(BPGConfig.class, GsonConfigSerializer::new);
        }
    }

    /**
     * Check whether the Cloth Config mod is installed
     *
     * @return {@link Boolean True} if Cloth Config is installed
     */
    public static boolean isClothConfigInstalled() {
        return FabricLoader.getInstance().isModLoaded("cloth-config");
    }

    /**
     * Get the {@link #CONFIG} instance or create a new one
     *
     * @return The {@link #CONFIG} instance
     */
    public static BPGConfig config() {
        if(CONFIG == null) {
            CONFIG = AutoConfig.getConfigHolder(BPGConfig.class).getConfig();
        }
        return CONFIG;
    }

}