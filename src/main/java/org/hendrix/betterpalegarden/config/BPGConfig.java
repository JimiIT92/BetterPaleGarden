package org.hendrix.betterpalegarden.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.hendrix.betterpalegarden.BetterPaleGarden;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link ConfigData Configuration class}
 */
@Config(name = BetterPaleGarden.MOD_ID)
public final class BPGConfig implements ConfigData {

    /**
     * Whether fog should be enabled
     */
    @ConfigEntry.Gui.Tooltip
    public boolean ENABLE_FOG = true;

    /**
     * The {@link Integer Fog thickness}
     */
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1L, max = BetterPaleGarden.MAX_FOG_THICKNESS)
    public long FOG_THICKNESS = 96L;

}