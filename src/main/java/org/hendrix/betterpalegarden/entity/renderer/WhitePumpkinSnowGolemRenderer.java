package org.hendrix.betterpalegarden.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SnowGolemEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.hendrix.betterpalegarden.block.WhitePumpkinBlock;
import org.hendrix.betterpalegarden.core.BPGBlocks;

/**
 * Renderer class for a {@link SnowGolemEntity Snow Golem} wearing a {@link WhitePumpkinBlock White Pumpkin Block}
 */
@Environment(EnvType.CLIENT)
public final class WhitePumpkinSnowGolemRenderer extends SnowGolemEntityRenderer {

    /**
     * Constructor. Set the {@link EntityRendererFactory.Context Renderer Context}
     *
     * @param context The {@link EntityRendererFactory.Context Renderer Context}
     */
    public WhitePumpkinSnowGolemRenderer(final EntityRendererFactory.Context context) {
        super(context);
    }

    /**
     * Render the {@link WhitePumpkinBlock White Pumpkin Block} on the {@link SnowGolemEntity Snow Golem} head
     *
     * @param snowGolemEntity The {@link EntityRendererFactory.Context Renderer Context}
     * @param livingEntityRenderState The {@link LivingEntityRenderState Living Entity Render State}
     * @param tickDelta The {@link Float delta amount of ticks}
     */
    @Override
    public void updateRenderState(final SnowGolemEntity snowGolemEntity, final LivingEntityRenderState livingEntityRenderState, final float tickDelta) {
        super.updateRenderState(snowGolemEntity, livingEntityRenderState, tickDelta);
        livingEntityRenderState.equippedHeadStack = snowGolemEntity.hasPumpkin() ? new ItemStack(BPGBlocks.CARVED_WHITE_PUMPKIN) : ItemStack.EMPTY;
        livingEntityRenderState.equippedHeadItemModel = this.itemRenderer.getModel(livingEntityRenderState.equippedHeadStack, snowGolemEntity, ModelTransformationMode.HEAD);
    }

}