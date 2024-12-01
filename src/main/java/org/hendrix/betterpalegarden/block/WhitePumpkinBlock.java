package org.hendrix.betterpalegarden.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.hendrix.betterpalegarden.core.BPGBlocks;

/**
 * Implementation class for a {@link Block White Pumpkin Block}
 */
public final class WhitePumpkinBlock extends Block {

    /**
     * The {@link MapCodec<WhitePumpkinBlock> White Pumpkin Block Codec}
     */
    public static final MapCodec<WhitePumpkinBlock> CODEC = createCodec(WhitePumpkinBlock::new);

    /**
     * Constructor. Set the {@link Block Block} properties
     *
     * @param settings The {@link Block Block} properties
     */
    public WhitePumpkinBlock(final AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * Carve the White Pumpkin when interacting with some {@link Items#SHEARS Shears}
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
        if (!stack.isOf(Items.SHEARS)) {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        }
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        final Direction hitSide = hit.getSide();
        final Direction pumpkinFaceDirection = hitSide.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : hitSide;
        world.playSound(null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.setBlockState(pos, BPGBlocks.CARVED_WHITE_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, pumpkinFaceDirection), Block.NOTIFY_ALL_AND_REDRAW);
        final ItemEntity itemEntity = new ItemEntity(
                world,
                (double)pos.getX() + 0.5 + (double)pumpkinFaceDirection.getOffsetX() * 0.65,
                (double)pos.getY() + 0.1,
                (double)pos.getZ() + 0.5 + (double)pumpkinFaceDirection.getOffsetZ() * 0.65,
                new ItemStack(Items.PUMPKIN_SEEDS, 4)
        );
        itemEntity.setVelocity(0.05 * (double)pumpkinFaceDirection.getOffsetX() + world.random.nextDouble() * 0.02, 0.05, 0.05 * (double)pumpkinFaceDirection.getOffsetZ() + world.random.nextDouble() * 0.02);
        world.spawnEntity(itemEntity);
        stack.damage(1, player, LivingEntity.getSlotForHand(hand));
        world.emitGameEvent(player, GameEvent.SHEAR, pos);
        player.incrementStat(Stats.USED.getOrCreateStat(Items.SHEARS));
        return ActionResult.SUCCESS;
    }

    /**
     * Get the {@link MapCodec<WhitePumpkinBlock> White Pumpkin Block Codec}
     *
     * @return The {@link #CODEC White Pumpkin Block Codec}
     */
    @Override
    public MapCodec<WhitePumpkinBlock> getCodec() {
        return CODEC;
    }

}