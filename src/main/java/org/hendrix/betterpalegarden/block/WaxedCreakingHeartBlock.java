package org.hendrix.betterpalegarden.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.enums.CreakingHeartState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

/**
 * Implementation class for a {@link CreakingHeartBlock Waxed Creaking Heart Block}
 */
public class WaxedCreakingHeartBlock extends Block {

    /**
     * The {@link MapCodec<WaxedCreakingHeartBlock> Waxed Creaking Heart Block Codec}
     */
    public static final MapCodec<WaxedCreakingHeartBlock> CODEC = createCodec(WaxedCreakingHeartBlock::new);

    /**
     * Constructor. Set the {@link Block Block} properties
     *
     * @param settings The {@link Block Block} properties
     */
    public WaxedCreakingHeartBlock(final Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(CreakingHeartBlock.AXIS, Direction.Axis.Y).with(CreakingHeartBlock.ACTIVE, CreakingHeartState.UPROOTED).with(CreakingHeartBlock.NATURAL, false));
    }

    /**
     * Remove wax from the {@link Block Block} when a {@link PlayerEntity Player} interacts with an {@link AxeItem Axe}
     *
     * @param stack The {@link ItemStack Item Stack} used to interact with the {@link Block Block}
     * @param state The {@link BlockState current Block State}
     * @param world The {@link World World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @param player The {@link PlayerEntity Player} interacting with the {@link Block Block}
     * @param hand The {@link Hand Hand} the {@link PlayerEntity Player} used to interact with the {@link Block Block}
     * @param hit The {@link BlockHitResult Block Hit Result}
     * @return The {@link ActionResult Action Result}
     */
    @Override
    protected ActionResult onUseWithItem(final ItemStack stack, final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockHitResult hit) {
        if (!(stack.getItem() instanceof AxeItem axeItem)) {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        }
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (player instanceof ServerPlayerEntity) {
            Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)player, pos, stack);
        }
        final BlockState strippedBlockState = getUnwaxedBlockState(state, world, pos);
        world.playSound(null, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.setBlockState(pos, strippedBlockState, Block.NOTIFY_ALL_AND_REDRAW);
        stack.damage(1, player, LivingEntity.getSlotForHand(hand));
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, strippedBlockState));
        world.syncWorldEvent(player, WorldEvents.BLOCK_SCRAPED, pos, 0);
        return ActionResult.SUCCESS;
    }

    /**
     * Get the {@link BlockState unwaxed Block State}
     *
     * @param state The {@link BlockState current Block State}
     * @param world The {@link World World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @return The {@link BlockState unwaxed Block State}
     */
    private BlockState getUnwaxedBlockState(final BlockState state, final World world, final BlockPos pos) {
        return Blocks.CREAKING_HEART.getDefaultState().with(CreakingHeartBlock.AXIS, state.get(CreakingHeartBlock.AXIS)).with(CreakingHeartBlock.ACTIVE, CreakingHeartBlock.shouldBeEnabled(state, world, pos) ? CreakingHeartState.AWAKE : CreakingHeartState.UPROOTED).with(CreakingHeartBlock.NATURAL, state.get(CreakingHeartBlock.NATURAL));
    }

    /**
     * Rotate the {@link BlockState current Block State}
     *
     * @param state The {@link BlockState current Block State}
     * @param rotation The {@link BlockRotation Block Rotation}
     * @return The {@link BlockState rotated Block State}
     */
    @Override
    protected BlockState rotate(final BlockState state, final BlockRotation rotation) {
        return PillarBlock.changeRotation(state, rotation);
    }

    /**
     * Add the {@link CreakingHeartBlock#AXIS Axis property} to the {@link BlockState Block State definition}
     *
     * @param stateBuilder The {@link StateManager.Builder Block State Builder}
     */
    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(CreakingHeartBlock.AXIS, CreakingHeartBlock.ACTIVE, CreakingHeartBlock.NATURAL);
    }

    /**
     * Get the {@link MapCodec<WaxedCreakingHeartBlock> Waxed Creaking Heart Block Codec}
     *
     * @return The {@link #CODEC Waxed Creaking Heart Block Codec}
     */
    @Override
    public MapCodec<WaxedCreakingHeartBlock> getCodec() {
        return CODEC;
    }

}