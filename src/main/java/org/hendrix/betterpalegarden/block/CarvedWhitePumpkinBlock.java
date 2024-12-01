package org.hendrix.betterpalegarden.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.hendrix.betterpalegarden.core.BPGBlocks;
import org.hendrix.betterpalegarden.core.BPGEntities;
import org.hendrix.betterpalegarden.entity.WhitePumpkinSnowGolemEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Implementation class for a {@link Block Carved White Pumpkin Block}
 */
public final class CarvedWhitePumpkinBlock extends HorizontalFacingBlock {

    /**
     * The {@link MapCodec<CarvedWhitePumpkinBlock> Carved White Pumpkin Block Codec}
     */
    public static final MapCodec<CarvedWhitePumpkinBlock> CODEC = createCodec(CarvedWhitePumpkinBlock::new);

    /**
     * The {@link EnumProperty<Direction> Block Facing Property}
     */
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    /**
     * The {@link BlockPattern Snow Golem Dispenser Pattern}
     */
    @Nullable
    private BlockPattern snowGolemDispenserPattern;
    /**
     * The {@link BlockPattern Snow Golem Pattern}
     */
    @Nullable
    private BlockPattern snowGolemPattern;
    /**
     * The {@link BlockPattern Iron Golem Dispenser Pattern}
     */
    @Nullable
    private BlockPattern ironGolemDispenserPattern;
    /**
     * The {@link BlockPattern Iron Golem Pattern}
     */
    @Nullable
    private BlockPattern ironGolemPattern;
    /**
     * The {@link Predicate<BlockState> Block State predicate} indicating whether the block is a golem head
     */
    private static final Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = state -> state != null && (state.isOf(BPGBlocks.CARVED_WHITE_PUMPKIN) || state.isOf(BPGBlocks.SOUL_O_LANTERN));

    /**
     * Constructor. Set the {@link Block Block} properties
     *
     * @param settings The {@link Block Block} properties
     */
    public CarvedWhitePumpkinBlock(final Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    /**
     * Try to spawn a golem when the {@link Block Block} is added to the {@link World World}
     *
     * @param state The {@link BlockState current Block State}
     * @param world The {@link World World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @param oldState The {@link BlockState previous Block State}
     * @param notify Whether surrounding {@link Block Blocks} should be notified
     */
    @Override
    protected void onBlockAdded(final BlockState state, final World world, final BlockPos pos, final BlockState oldState, final boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            this.trySpawnEntity(world, pos);
        }
    }

    /**
     * Check if the {@link Block Block} can be dispensed from a {@link DispenserBlock Dispenser Block}
     *
     * @param world The {@link World World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @return {@link Boolean True} if the {@link Block Block} can be dispensed
     */
    public boolean canDispense(final WorldView world, final BlockPos pos) {
        return this.getSnowGolemDispenserPattern().searchAround(world, pos) != null || this.getIronGolemDispenserPattern().searchAround(world, pos) != null;
    }

    /**
     * Try spawning a golem
     *
     * @param world The {@link World World reference}
     * @param pos The {@link BlockPos current Block Pos}
     */
    private void trySpawnEntity(final World world, final BlockPos pos) {
        final BlockPattern.Result snowGolemPatternResult = this.getSnowGolemPattern().searchAround(world, pos);
        if (snowGolemPatternResult != null) {
            final WhitePumpkinSnowGolemEntity snowGolemEntity = BPGEntities.SNOW_GOLEM.create(world, SpawnReason.TRIGGERED);
            if (snowGolemEntity != null) {
                spawnEntity(world, snowGolemPatternResult, snowGolemEntity, snowGolemPatternResult.translate(0, 2, 0).getBlockPos());
            }
        } else {
            final BlockPattern.Result ironGolemPatternResult = this.getIronGolemPattern().searchAround(world, pos);
            if (ironGolemPatternResult != null) {
                final IronGolemEntity ironGolemEntity = EntityType.IRON_GOLEM.create(world, SpawnReason.TRIGGERED);
                if (ironGolemEntity != null) {
                    ironGolemEntity.setPlayerCreated(true);
                    spawnEntity(world, ironGolemPatternResult, ironGolemEntity, ironGolemPatternResult.translate(1, 2, 0).getBlockPos());
                }
            }
        }
    }

    /**
     * Spawn a golem
     *
     * @param world The {@link World World reference}
     * @param patternResult The {@link BlockPattern.Result Block Pattern Result}
     * @param entity The {@link Entity Entity to spawn}
     * @param pos The {@link BlockPos Entity Block Pos}
     */
    private static void spawnEntity(final World world, final BlockPattern.Result patternResult, final Entity entity, final BlockPos pos) {
        breakPatternBlocks(world, patternResult);
        entity.refreshPositionAndAngles((double)pos.getX() + 0.5, (double)pos.getY() + 0.05, (double)pos.getZ() + 0.5, 0.0F, 0.0F);
        world.spawnEntity(entity);
        world.getNonSpectatingEntities(ServerPlayerEntity.class, entity.getBoundingBox().expand(5.0)).forEach(serverPlayerEntity -> Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity));
        updatePatternBlocks(world, patternResult);
    }

    /**
     * Break all {@link Block Blocks} in a golem pattern
     *
     * @param world The {@link World World reference}
     * @param patternResult The {@link BlockPattern.Result Block Pattern Result}
     */
    public static void breakPatternBlocks(final World world, final BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); i++) {
            for (int j = 0; j < patternResult.getHeight(); j++) {
                final CachedBlockPosition blockPos = patternResult.translate(i, j, 0);
                world.setBlockState(blockPos.getBlockPos(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos.getBlockPos(), Block.getRawIdFromState(blockPos.getBlockState()));
            }
        }
    }

    /**
     * Update all {@link Block Blocks} in a golem pattern
     *
     * @param world The {@link World World reference}
     * @param patternResult The {@link BlockPattern.Result Block Pattern Result}
     */
    public static void updatePatternBlocks(final World world, final BlockPattern.Result patternResult) {
        for (int i = 0; i < patternResult.getWidth(); i++) {
            for (int j = 0; j < patternResult.getHeight(); j++) {
                world.updateNeighbors(patternResult.translate(i, j, 0).getBlockPos(), Blocks.AIR);
            }
        }
    }

    /**
     * Get the {@link BlockState default Block Placement State}
     *
     * @param itemPlacementContext The {@link ItemPlacementContext Item Placement Context}
     * @return The {@link BlockState Placement Block State}
     */
    @Override
    public BlockState getPlacementState(final ItemPlacementContext itemPlacementContext) {
        return this.getDefaultState().with(FACING, itemPlacementContext.getHorizontalPlayerFacing().getOpposite());
    }

    /**
     * Add the {@link #FACING Facing property} to the {@link BlockState Block State definition}
     *
     * @param stateBuilder The {@link StateManager.Builder Block State Builder}
     */
    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
    }

    /**
     * Get the {@link BlockPattern Snow Golem Dispenser Pattern}
     *
     * @return The {@link BlockPattern Snow Golem Dispenser Pattern}
     */
    private BlockPattern getSnowGolemDispenserPattern() {
        if (this.snowGolemDispenserPattern == null) {
            this.snowGolemDispenserPattern = BlockPatternBuilder.start()
                    .aisle(" ", "#", "#")
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                    .build();
        }
        return this.snowGolemDispenserPattern;
    }

    /**
     * Get the {@link BlockPattern Snow Golem Pattern}
     *
     * @return The {@link BlockPattern Snow Golem Pattern}
     */
    private BlockPattern getSnowGolemPattern() {
        if (this.snowGolemPattern == null) {
            this.snowGolemPattern = BlockPatternBuilder.start()
                    .aisle("^", "#", "#")
                    .where('^', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_PREDICATE))
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                    .build();
        }
        return this.snowGolemPattern;
    }

    /**
     * Get the {@link BlockPattern Iron Golem Dispenser Pattern}
     *
     * @return The {@link BlockPattern Iron Golem Dispenser Pattern}
     */
    private BlockPattern getIronGolemDispenserPattern() {
        if (this.ironGolemDispenserPattern == null) {
            this.ironGolemDispenserPattern = BlockPatternBuilder.start()
                    .aisle("~ ~", "###", "~#~")
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
                    .where('~', pos -> pos.getBlockState().isAir())
                    .build();
        }
        return this.ironGolemDispenserPattern;
    }

    /**
     * Get the {@link BlockPattern Iron Golem Pattern}
     *
     * @return The {@link BlockPattern Iron Golem Pattern}
     */
    private BlockPattern getIronGolemPattern() {
        if (this.ironGolemPattern == null) {
            this.ironGolemPattern = BlockPatternBuilder.start()
                    .aisle("~^~", "###", "~#~")
                    .where('^', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_PREDICATE))
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
                    .where('~', pos -> pos.getBlockState().isAir())
                    .build();
        }
        return this.ironGolemPattern;
    }

    /**
     * Get the {@link MapCodec<WhitePumpkinBlock> White Pumpkin Block Codec}
     *
     * @return The {@link #CODEC White Pumpkin Block Codec}
     */
    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }
}
