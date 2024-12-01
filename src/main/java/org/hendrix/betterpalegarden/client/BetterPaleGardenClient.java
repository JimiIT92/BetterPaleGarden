package org.hendrix.betterpalegarden.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.core.BPGEntities;
import org.hendrix.betterpalegarden.entity.renderer.WhitePumpkinSnowGolemRenderer;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden Hendrix's Better Pale Garden} {@link ClientModInitializer Client initializer}
 */
@Environment(EnvType.CLIENT)
public final class BetterPaleGardenClient implements ClientModInitializer {

    public static final EntityModelLayer MODEL_SNOW_GOLEM_LAYER = new EntityModelLayer(IdentifierUtils.modIdentifier("snow_golem"), "main");

    /**
     * Initialize the mod's client stuffs
     */
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(BPGEntities.SNOW_GOLEM, WhitePumpkinSnowGolemRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_SNOW_GOLEM_LAYER, SnowGolemEntityModel::getTexturedModelData);
    }

}