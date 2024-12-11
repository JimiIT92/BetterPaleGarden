package org.hendrix.betterpalegarden.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.entity.passive.SnowGolemEntity;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.core.BPGBlocks;
import org.hendrix.betterpalegarden.core.BPGEntities;
import org.hendrix.betterpalegarden.entity.renderer.WhitePumpkinSnowGolemRenderer;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.Arrays;

/**
 * {@link BetterPaleGarden Hendrix's Better Pale Garden} {@link ClientModInitializer Client initializer}
 */
@Environment(EnvType.CLIENT)
public final class BetterPaleGardenClient implements ClientModInitializer {

    /**
     * The {@link EntityModelLayer Entity Model Layer} for a {@link SnowGolemEntity White Pumpkin Snow Golem}
     */
    public static final EntityModelLayer MODEL_SNOW_GOLEM_LAYER = new EntityModelLayer(IdentifierUtils.modIdentifier("snow_golem"), "main");

    /**
     * Initialize the mod's client stuffs
     */
    @Override
    public void onInitializeClient() {
        Arrays.asList(
                BPGBlocks.THORN_BUSH,
                BPGBlocks.GLOWING_PUMPKIN,
                BPGBlocks.CHRYSANTHEMUM,
                BPGBlocks.POTTED_CHRYSANTHEMUM
        ).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped()));

        EntityRendererRegistry.register(BPGEntities.SNOW_GOLEM, WhitePumpkinSnowGolemRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_SNOW_GOLEM_LAYER, SnowGolemEntityModel::getTexturedModelData);
    }

}