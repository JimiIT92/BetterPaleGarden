package org.hendrix.betterpalegarden.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.animal.golem.SnowGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.client.renderer.entity.WhitePumpkinSnowGolemRenderer;
import org.hendrix.betterpalegarden.core.BPGEntityTypes;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden} {@link ClientModInitializer}
 */
@Environment(EnvType.CLIENT)
public final class BetterPaleGardenClient implements ClientModInitializer {

    public static final ModelLayerLocation SNOW_GOLEM = new ModelLayerLocation(IdentifierUtils.modded("snow_golem"), "main");
    /**
     * Initialize the mod's client stuffs
     */
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(SNOW_GOLEM, SnowGolemModel::createBodyLayer);
        EntityRenderers.register(BPGEntityTypes.SNOW_GOLEM, WhitePumpkinSnowGolemRenderer::new);
    }

}