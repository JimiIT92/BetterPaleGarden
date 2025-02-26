package org.hendrix.betterpalegarden.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * Implementation class for a {@link Block Glowing Pumpkin Block}
 */
public final class GlowingPumpkinBlock extends HorizontalFacingBlock {

    /**
     * The {@link MapCodec<GlowingPumpkinBlock> Glowing Pumpkin Block Codec}
     */
    public static final MapCodec<GlowingPumpkinBlock> CODEC = createCodec(GlowingPumpkinBlock::new);

    /**
     * The {@link EnumProperty < Direction > Block Facing Property}
     */
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    /**
     * The {@link BooleanProperty Pumpkin Open Property}
     */
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    /**
     * Constructor. Set the {@link Block Block} properties
     *
     * @param settings The {@link Block Block} properties
     */
    public GlowingPumpkinBlock(final Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    /**
     * Get the {@link BlockState default Block Placement State}
     *
     * @param itemPlacementContext The {@link ItemPlacementContext Item Placement Context}
     * @return The {@link BlockState Placement Block State}
     */
    @Override
    public BlockState getPlacementState(final ItemPlacementContext itemPlacementContext) {
        return this.getDefaultState()
                .with(FACING, itemPlacementContext.getHorizontalPlayerFacing().getOpposite())
                .with(OPEN, shouldOpen(itemPlacementContext.getWorld()));
    }

    /**
     * Check if the {@link HorizontalFacingBlock Glowing Pumpkin Block} should open
     *
     * @param world The {@link World World reference}
     * @return {@link Boolean True if the Block should open}
     */
    private Boolean shouldOpen(final World world) {
        return world.getDimension().natural() && !world.isDay();
    }

    /**
     * Add the {@link #FACING Facing property} to the {@link BlockState Block State definition}
     *
     * @param stateBuilder The {@link StateManager.Builder Block State Builder}
     */
    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING).add(OPEN);
    }

    /**
     * Make the {@link HorizontalFacingBlock Glowing Pumpkin} randomly tick
     *
     * @param state The {@link BlockState current Block State}
     * @param world The {@link ServerWorld World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @param random The {@link Random Random instance}
     */
    protected void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        this.updateStateAndNotifyOthers(state, world, pos, random);
        super.randomTick(state, world, pos, random);
    }

    /**
     * Make the {@link HorizontalFacingBlock Glowing Pumpkin} randomly tick
     *
     * @param state The {@link BlockState current Block State}
     * @param world The {@link ServerWorld World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @param random The {@link Random Random instance}
     */
    protected void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        this.updateStateAndNotifyOthers(state, world, pos, random);
        super.scheduledTick(state, world, pos, random);
    }

    /**
     * Make the {@link HorizontalFacingBlock Glowing Pumpkin} randomly tick
     *
     * @param state The {@link BlockState current Block State}
     * @param world The {@link ServerWorld World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @param random The {@link Random Random instance}
     */
    private void updateStateAndNotifyOthers(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        final boolean isOpen = state.get(OPEN);
        if((this.shouldOpen(world) && !isOpen) || (!this.shouldOpen(world) && isOpen)) {
            world.setBlockState(pos, state.with(OPEN, !isOpen), 3);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
            BlockPos.iterate(pos.add(-3, -2, -3), pos.add(3, 2, 3)).forEach((otherPos) -> {
                final BlockState neighborState = world.getBlockState(otherPos);
                if (neighborState == state) {
                    final double distance = Math.sqrt(pos.getSquaredDistance(otherPos));
                    final int randomTicks = random.nextBetween((int)(distance * (double)5.0F), (int)(distance * (double)10.0F));
                    world.scheduleBlockTick(otherPos, state.getBlock(), randomTicks);
                }
            });
        }
    }

    /**
     * Get the {@link MapCodec<GlowingPumpkinBlock> Glowing Pumpkin Block Codec}
     *
     * @return The {@link #CODEC Glowing Pumpkin Block Codec}
     */
    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

}