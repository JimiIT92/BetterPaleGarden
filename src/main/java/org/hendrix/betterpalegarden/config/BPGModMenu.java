package org.hendrix.betterpalegarden.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.hendrix.betterpalegarden.BetterPaleGarden;

/**
 * {@link BetterPaleGarden} {@link ModMenuApi Mod Menu Api integration}
 */
@Environment(EnvType.CLIENT)
public final class BPGModMenu implements ModMenuApi {

    /**
     * Get the mod's config screen
     *
     * @return The mod's config screen
     */
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BetterPaleGarden.isClothConfigInstalled() ? parent -> AutoConfigClient.getConfigScreen(BPGConfig.class, parent).get() : null;
    }
}