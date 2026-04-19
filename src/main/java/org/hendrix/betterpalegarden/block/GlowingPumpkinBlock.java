package org.hendrix.betterpalegarden.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.NonNull;

/**
 * Implementation class for a glowing pumpkin
 */
public final class GlowingPumpkinBlock extends HorizontalDirectionalBlock {

    /**
     * The {@link MapCodec<GlowingPumpkinBlock> Carved White Pumpkin} Codec
     */
    public static final MapCodec<GlowingPumpkinBlock> CODEC = simpleCodec(GlowingPumpkinBlock::new);

    /**
     * The block's facing property
     */
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    /**
     * The block's open property
     */
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    /**
     * Constructor. Set the {@link BlockBehaviour.Properties}
     *
     * @param properties The {@link BlockBehaviour.Properties}
     */
    public GlowingPumpkinBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    /**
     * Get the placed {@link BlockState}
     *
     * @param context The {@link BlockPlaceContext}
     * @return The placed {@link BlockState}
     */
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(OPEN, shouldOpen(context.getLevel()));
    }

    /**
     * Create the {@link BlockState} definition
     *
     * @param builder The {@link StateDefinition.Builder}
     */
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    /**
     * Tick the block
     *
     * @param state The current {@link BlockState}
     * @param level The {@link ServerLevel} reference
     * @param pos The current {@link BlockPos}
     * @param random The {@link RandomSource}
     */
    @Override
    protected void randomTick(final @NonNull BlockState state, final @NonNull ServerLevel level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
        this.updateStateAndNotifyOthers(state, level, pos, random);
        super.randomTick(state, level, pos, random);
    }

    /**
     * Tick the block
     *
     * @param state The current {@link BlockState}
     * @param level The {@link Level} reference
     * @param pos The current {@link BlockPos}
     * @param random The {@link RandomSource}
     */
    @Override
    public void animateTick(final @NonNull BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
        this.updateStateAndNotifyOthers(state, level, pos, random);
        super.animateTick(state, level, pos, random);
    }

    /**
     * Make the glowing pumpkin open and notify nearby glowing pumpkins
     *
     * @param state The current {@link BlockState}
     * @param level The {@link ServerLevel} reference
     * @param pos The current {@link BlockPos}
     * @param random The {@link RandomSource}
     */
    private void updateStateAndNotifyOthers(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        final boolean isOpen = state.getValue(OPEN);
        if((this.shouldOpen(level) && !isOpen) || (!this.shouldOpen(level) && isOpen)) {
            level.setBlockAndUpdate(pos, state.setValue(OPEN, !isOpen));
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
            BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3)).forEach((nearby) -> {
                final BlockState nearbyState = level.getBlockState(nearby);
                if (nearbyState == state) {
                    final double distance = Math.sqrt(pos.distSqr(nearby));
                    final int delay = random.nextIntBetweenInclusive((int)(distance * (double)5.0F), (int)(distance * (double)10.0F));
                    level.scheduleTick(nearby, state.getBlock(), delay);
                }
            });
        }
    }

    /**
     * Check whether the glowing pumpkin should open
     *
     * @param level The {@link Level} reference
     * @return {@link Boolean True} if it should open
     */
    private boolean shouldOpen(final Level level) {
        return level.isDarkOutside() && level.dimensionType().hasSkyLight();
    }

    /**
     * Get the block's codec
     *
     * @return The block's codec
     */
    @Override
    public @NonNull MapCodec<? extends GlowingPumpkinBlock> codec() {
        return CODEC;
    }
}