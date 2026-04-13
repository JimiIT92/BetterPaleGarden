package org.hendrix.betterpalegarden.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.BlockHitResult;
import org.hendrix.betterpalegarden.core.BPGBlocks;
import org.jspecify.annotations.NonNull;

/**
 * Implementation class for a white pumpkin block
 */
public final class WhitePumpkinBlock extends Block {

    /**
     * The {@link MapCodec<WhitePumpkinBlock> White Pumpkin} Codec
     */
    public static final MapCodec<WhitePumpkinBlock> CODEC = simpleCodec(WhitePumpkinBlock::new);

    /**
     * Constructor. Set the {@link BlockBehaviour.Properties}
     *
     * @param properties The {@link BlockBehaviour.Properties}
     */
    public WhitePumpkinBlock(final Properties properties) {
        super(properties);
    }

    /**
     * Carve the pumpkin when interacting with some shears
     *
     * @param itemStack The {@link ItemStack} used to interact with the block
     * @param state The current {@link BlockState}
     * @param level The {@link Level} reference
     * @param pos The current {@link BlockPos}
     * @param player The {@link Player} that interacted with the block
     * @param hand The {@link InteractionHand} used to interact with the block
     * @param hitResult The {@link BlockHitResult}
     * @return The {@link InteractionResult}
     */
    @Override
    protected @NonNull InteractionResult useItemOn(final ItemStack itemStack, final @NonNull BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final @NonNull Player player, final @NonNull InteractionHand hand, final @NonNull BlockHitResult hitResult) {
        if (!itemStack.is(Items.SHEARS)) {
            return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
        }
        if (level instanceof ServerLevel serverLevel) {
            final Direction clickedDirection = hitResult.getDirection();
            final Direction direction = clickedDirection.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : clickedDirection;
            dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.CARVE_PUMPKIN, state, level.getBlockEntity(pos), itemStack, player, (ignored, pumpkinSeeds) -> {
                final ItemEntity entity = new ItemEntity(level, (double)pos.getX() + (double)0.5F + (double)direction.getStepX() * 0.65, (double)pos.getY() + 0.1, (double)pos.getZ() + (double)0.5F + (double)direction.getStepZ() * 0.65, pumpkinSeeds);
                final RandomSource random = level.getRandom();
                entity.setDeltaMovement(0.05 * (double)direction.getStepX() + random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction.getStepZ() + random.nextDouble() * 0.02);
                level.addFreshEntity(entity);
            });
            level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, BPGBlocks.CARVED_WHITE_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
            itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
            level.gameEvent(player, GameEvent.SHEAR, pos);
            player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Get the block's codec
     *
     * @return The block's codec
     */
    @Override
    public @NonNull MapCodec<WhitePumpkinBlock> codec() {
        return CODEC;
    }

}