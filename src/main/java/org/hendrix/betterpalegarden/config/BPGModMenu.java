package org.hendrix.betterpalegarden.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.hendrix.betterpalegarden.BetterPaleGarden;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link ModMenuApi Mod Menu Api integration}
 */
@Environment(EnvType.CLIENT)
public final class BPGModMenu implements ModMenuApi {

    /**
     * Get the {@link ConfigScreenFactory Mod Config Screen Factory}
     *
     * @return The {@link ConfigScreenFactory Mod Config Screen Factory}
     */
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BetterPaleGarden.isClothConfigInstalled() ? parent -> AutoConfig.getConfigScreen(BPGConfig.class, parent).get() : null;
    }

}