package org.hendrix.betterpalegarden.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.client.render.entity.state.SnowGolemEntityRenderState;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.Identifier;
import org.hendrix.betterpalegarden.block.WhitePumpkinBlock;

/**
 * Renderer class for a {@link SnowGolemEntity Snow Golem} wearing a {@link WhitePumpkinBlock White Pumpkin Block}
 */
@Environment(EnvType.CLIENT)
public final class WhitePumpkinSnowGolemRenderer extends MobEntityRenderer<SnowGolemEntity, SnowGolemEntityRenderState, SnowGolemEntityModel> {

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/snow_golem.png");

    /**
     * Constructor. Set the {@link EntityRendererFactory.Context Renderer Context}
     *
     * @param context The {@link EntityRendererFactory.Context Renderer Context}
     */
    public WhitePumpkinSnowGolemRenderer(final EntityRendererFactory.Context context) {
        super(context, new SnowGolemEntityModel(context.getPart(EntityModelLayers.SNOW_GOLEM)), 0.5F);
        this.addFeature(new SnowGolemWhitePumpkinFeatureRenderer(this, context.getBlockRenderManager()));
    }

    /**
     * Get the {@link Identifier Entity Texture Identifier}
     *
     * @param snowGolemEntityRenderState The {@link SnowGolemEntityRenderState Snow Golem Entity Render State}
     * @return The {@link #TEXTURE Entity Texture Identifier}
     */
    public Identifier getTexture(final SnowGolemEntityRenderState snowGolemEntityRenderState) {
        return TEXTURE;
    }

    /**
     * Get the {@link SnowGolemEntityRenderState Entity Render State}
     *
     * @return The {@link SnowGolemEntityRenderState Entity Render State}
     */
    public SnowGolemEntityRenderState createRenderState() {
        return new SnowGolemEntityRenderState();
    }

    /**
     * Render the Entity
     *
     * @param snowGolemEntity The {@link SnowGolemEntity Snow Golem Entity}
     * @param snowGolemEntityRenderState The {@link SnowGolemEntityRenderState Entity Render State}
     * @param partialTicks The {@link Float partial ticks amount}
     */
    public void updateRenderState(SnowGolemEntity snowGolemEntity, SnowGolemEntityRenderState snowGolemEntityRenderState, float partialTicks) {
        super.updateRenderState(snowGolemEntity, snowGolemEntityRenderState, partialTicks);
        snowGolemEntityRenderState.hasPumpkin = snowGolemEntity.hasPumpkin();
    }

}