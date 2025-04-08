package org.hendrix.betterpalegarden.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SnowGolemPumpkinFeatureRenderer;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.client.render.entity.state.SnowGolemEntityRenderState;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.hendrix.betterpalegarden.core.BPGBlocks;

/**
 * Implementation class for the {@link SnowGolemPumpkinFeatureRenderer Snow Golem White Pumpkin rendering}
 */
@Environment(EnvType.CLIENT)
public final class SnowGolemWhitePumpkinFeatureRenderer extends FeatureRenderer<SnowGolemEntityRenderState, SnowGolemEntityModel> {

    /**
     * The {@link BlockRenderManager Block Renderer Manager}
     */
    private final BlockRenderManager blockRenderManager;

    /**
     * Constructor. Set the feature properties
     *
     * @param context The {@link FeatureRendererContext Feature Renderer Context}
     * @param blockRenderManager The {@link BlockRenderManager Block Renderer Manager}
     */
    public SnowGolemWhitePumpkinFeatureRenderer(final FeatureRendererContext<SnowGolemEntityRenderState, SnowGolemEntityModel> context, final BlockRenderManager blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }

    /**
     * Render the {@link BPGBlocks#WHITE_PUMPKIN White Pumpkin}
     *
     * @param matrixStack The {@link MatrixStack Render Matrix Stack}
     * @param vertexConsumerProvider The {@link VertexConsumerProvider Vertex Consumer Provider}
     * @param light The {@link Integer client light}
     * @param snowGolemEntityRenderState The {@link SnowGolemEntityRenderState Snow Golem Entity Render State}
     * @param yaw The {@link Float mob Yaw}
     * @param pitch The {@link Float mob Pitch}
     */
    public void render(final MatrixStack matrixStack, final VertexConsumerProvider vertexConsumerProvider, final int light, final SnowGolemEntityRenderState snowGolemEntityRenderState, final float yaw, final float pitch) {
        if (snowGolemEntityRenderState.hasPumpkin) {
            if (!snowGolemEntityRenderState.invisible || snowGolemEntityRenderState.hasOutline) {
                matrixStack.push();
                this.getContextModel().getHead().applyTransform(matrixStack);
                final float scale = 0.625F;
                matrixStack.translate(0.0F, -0.34375F, 0.0F);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                matrixStack.scale(scale, -scale, -scale);
                final BlockState blockState = BPGBlocks.CARVED_WHITE_PUMPKIN.getDefaultState();
                matrixStack.translate(-0.5F, -0.5F, -0.5F);
                final VertexConsumer vertexConsumer = snowGolemEntityRenderState.hasOutline && snowGolemEntityRenderState.invisible ? vertexConsumerProvider.getBuffer(RenderLayer.getOutline(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)) : vertexConsumerProvider.getBuffer(RenderLayers.getEntityBlockLayer(blockState));
                BlockModelRenderer.render(matrixStack.peek(), vertexConsumer, this.blockRenderManager.getModel(blockState), 0.0F, 0.0F, 0.0F, light, LivingEntityRenderer.getOverlay(snowGolemEntityRenderState, 0.0F));
                matrixStack.pop();
            }
        }
    }

}