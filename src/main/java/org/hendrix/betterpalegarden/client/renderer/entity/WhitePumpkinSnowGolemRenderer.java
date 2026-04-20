package org.hendrix.betterpalegarden.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SnowGolemRenderer;
import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
import net.minecraft.world.entity.animal.golem.SnowGolem;
 import org.hendrix.betterpalegarden.core.BPGBlocks;
import org.jspecify.annotations.NonNull;

/**
 * Renderer class for a Snow Golem wearing a white pumpkin
 */
@Environment(EnvType.CLIENT)
public class WhitePumpkinSnowGolemRenderer extends SnowGolemRenderer {

    /**
     * Constructor. Set the renderer context
     *
     * @param context The entity renderer context
     */
    public WhitePumpkinSnowGolemRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    /**
     * Render the golem pumpkin
     *
     * @param entity The {@link SnowGolem} reference
     * @param state The {@link SnowGolemRenderState} reference
     * @param partialTicks The entity partial ticks
     */
    @Override
    public void extractRenderState(final @NonNull SnowGolem entity, final @NonNull SnowGolemRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (entity.hasPumpkin()) {
            this.blockModelResolver.update(state.headBlock, BPGBlocks.CARVED_WHITE_PUMPKIN.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
        } else {
            state.headBlock.clear();
        }

    }

}