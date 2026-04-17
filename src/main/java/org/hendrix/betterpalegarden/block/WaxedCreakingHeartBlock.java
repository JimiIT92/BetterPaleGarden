package org.hendrix.betterpalegarden.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jspecify.annotations.NonNull;

/**
 * Implementation class for a waxed {@link CreakingHeartBlock}
 */
public class WaxedCreakingHeartBlock extends Block {

    /**
     * The {@link MapCodec<WaxedCreakingHeartBlock> Waxed Creaking Heart} Codec
     */
    public static final MapCodec<WaxedCreakingHeartBlock> CODEC = simpleCodec(WaxedCreakingHeartBlock::new);
    /**
     * The block's axis property
     */
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    /**
     * The block's creaking heart state property
     */
    public static final EnumProperty<CreakingHeartState> STATE = BlockStateProperties.CREAKING_HEART_STATE;
    /**
     * The block's "natural" property
     */
    public static final BooleanProperty NATURAL = BlockStateProperties.NATURAL;

    /**
     * Constructor. Set the {@link BlockBehaviour.Properties}
     *
     * @param properties The {@link BlockBehaviour.Properties}
     */
    public WaxedCreakingHeartBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(STATE, CreakingHeartState.UPROOTED).setValue(NATURAL, false));
    }

    /**
     * Rotate the block
     *
     * @param state The current {@link BlockState}
     * @param rotation The block {@link Rotation}
     * @return The rotated {@link BlockState}
     */
    protected @NonNull BlockState rotate(final @NonNull BlockState state, final @NonNull Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(state, rotation);
    }

    /**
     * Create the {@link BlockState} definition
     *
     * @param builder The {@link StateDefinition.Builder}
     */
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, STATE, NATURAL);
    }

    /**
     * Get the block's codec
     *
     * @return The block's codec
     */
    @Override
    public @NonNull MapCodec<WaxedCreakingHeartBlock> codec() {
        return CODEC;
    }

}