package org.hendrix.betterpalegarden.block;

import com.google.common.collect.BiMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.hendrix.betterpalegarden.core.BPGBlocks;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implementation class for a carved white pumpkin
 */
public final class CarvedWhitePumpkinBlock extends HorizontalDirectionalBlock {

    /**
     * The {@link MapCodec<CarvedWhitePumpkinBlock> Carved White Pumpkin} Codec
     */
    public static final MapCodec<CarvedWhitePumpkinBlock> CODEC = simpleCodec(CarvedWhitePumpkinBlock::new);
    /**
     * The block's facing property
     */
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    /**
     * The snow golem base {@link BlockPattern}
     */
    private @Nullable BlockPattern snowGolemBase;
    /**
     * The snow golem full {@link BlockPattern}
     */
    private @Nullable BlockPattern snowGolemFull;
    /**
     * The iron golem base {@link BlockPattern}
     */
    private @Nullable BlockPattern ironGolemBase;
    /**
     * The iron golem full {@link BlockPattern}
     */
    private @Nullable BlockPattern ironGolemFull;
    /**
     * The copper golem base {@link BlockPattern}
     */
    private @Nullable BlockPattern copperGolemBase;
    /**
     * The copper golem base {@link BlockPattern}
     */
    private @Nullable BlockPattern copperGolemFull;
    /**
     * Check whether a block is valid for spawning a golem
     */
    private static final Predicate<BlockState> PUMPKINS_PREDICATE = (input) -> input.is(BPGBlocks.CARVED_WHITE_PUMPKIN) || input.is(BPGBlocks.SOUL_O_LANTERN);

    /**
     * Constructor. Set the {@link BlockBehaviour.Properties}
     *
     * @param properties The {@link BlockBehaviour.Properties}
     */
    public CarvedWhitePumpkinBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * Try spawning a golem when the block is placed
     *
     * @param state The current {@link BlockState}
     * @param level The {@link Level} reference
     * @param pos The current {@link BlockPos}
     * @param oldState The previous {@link BlockState}
     * @param movedByPiston Whether the block has been moved by a piston
     */
    protected void onPlace(final BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnGolem(level, pos);
        }
    }

    /**
     * Check whether a golem can be spawned
     *
     * @param level The {@link Level} reference
     * @param topPos The pumpkin {@link BlockPos}
     * @return {@link Boolean True} if a golem can be spawned
     */
    public boolean canSpawnGolem(final LevelReader level, final BlockPos topPos) {
        return this.getOrCreateSnowGolemBase().find(level, topPos) != null || this.getOrCreateIronGolemBase().find(level, topPos) != null || this.getOrCreateCopperGolemBase().find(level, topPos) != null;
    }

    /**
     * Try to spawn a golem
     *
     * @param level The {@link Level} reference
     * @param topPos The pumpkin {@link BlockPos}
     */
    private void trySpawnGolem(final Level level, final BlockPos topPos) {
        final BlockPattern.BlockPatternMatch snowGolemMatch = this.getOrCreateSnowGolemFull().find(level, topPos);
        if (snowGolemMatch != null) {
            final SnowGolem snowGolem = EntityType.SNOW_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
            if (snowGolem != null) {
                spawnGolemInWorld(level, snowGolemMatch, snowGolem, snowGolemMatch.getBlock(0, 2, 0).getPos());
                return;
            }
        }

        final BlockPattern.BlockPatternMatch ironGolemMatch = this.getOrCreateIronGolemFull().find(level, topPos);
        if (ironGolemMatch != null) {
            final IronGolem ironGolem = EntityType.IRON_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
            if (ironGolem != null) {
                ironGolem.setPlayerCreated(true);
                spawnGolemInWorld(level, ironGolemMatch, ironGolem, ironGolemMatch.getBlock(1, 2, 0).getPos());
                return;
            }
        }

        final BlockPattern.BlockPatternMatch copperGolemMatch = this.getOrCreateCopperGolemFull().find(level, topPos);
        if (copperGolemMatch != null) {
            final CopperGolem copperGolem = EntityType.COPPER_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
            if (copperGolem != null) {
                spawnGolemInWorld(level, copperGolemMatch, copperGolem, copperGolemMatch.getBlock(0, 0, 0).getPos());
                this.replaceCopperBlockWithChest(level, copperGolemMatch);
                copperGolem.spawn(this.getWeatherStateFromPattern(copperGolemMatch));
            }
        }

    }

    /**
     * Get the copper golem weather state based on the copper golem pattern
     *
     * @param copperGolemMatch The {@link BlockPattern.BlockPatternMatch} for a copper golem
     * @return The {@link WeatheringCopper.WeatherState}
     */
    private WeatheringCopper.WeatherState getWeatherStateFromPattern(final BlockPattern.BlockPatternMatch copperGolemMatch) {
        final Block block = copperGolemMatch.getBlock(0, 1, 0).getState().getBlock();
        if (block instanceof WeatheringCopper copper) {
            return copper.getAge();
        }
        return Optional.ofNullable((Block)((BiMap<?, ?>) HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(block))
                .filter((weatheringCopper) -> weatheringCopper instanceof WeatheringCopper)
                .map((weatheringCopper) -> (WeatheringCopper)weatheringCopper)
                .orElse((WeatheringCopper) Blocks.COPPER_BLOCK).getAge();
    }

    /**
     * Spawn a golem
     *
     * @param level The {@link Level} reference
     * @param match The {@link BlockPattern.BlockPatternMatch}
     * @param golem The {@link Entity} to spawn
     * @param spawnPos The golem {@link BlockPos}
     */
    private static void spawnGolemInWorld(final Level level, final BlockPattern.BlockPatternMatch match, final Entity golem, final BlockPos spawnPos) {
        clearPatternBlocks(level, match);
        golem.snapTo((double)spawnPos.getX() + (double)0.5F, (double)spawnPos.getY() + 0.05, (double)spawnPos.getZ() + (double)0.5F, 0.0F, 0.0F);
        level.addFreshEntity(golem);

        for(ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0F))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(player, golem);
        }

        updatePatternBlocks(level, match);
    }

    /**
     * Clear the blocks used to spawn the golem
     *
     * @param level The {@link Level} reference
     * @param match The {@link BlockPattern.BlockPatternMatch}
     */
    public static void clearPatternBlocks(final Level level, final BlockPattern.BlockPatternMatch match) {
        for(int x = 0; x < match.getWidth(); ++x) {
            for(int y = 0; y < match.getHeight(); ++y) {
                BlockInWorld block = match.getBlock(x, y, 0);
                level.setBlock(block.getPos(), Blocks.AIR.defaultBlockState(), 2);
                level.levelEvent(2001, block.getPos(), Block.getId(block.getState()));
            }
        }
    }

    /**
     * Update the blocks used to spawn the golem
     *
     * @param level The {@link Level} reference
     * @param match The {@link BlockPattern.BlockPatternMatch}
     */
    public static void updatePatternBlocks(final Level level, final BlockPattern.BlockPatternMatch match) {
        for(int x = 0; x < match.getWidth(); ++x) {
            for(int y = 0; y < match.getHeight(); ++y) {
                level.updateNeighborsAt(match.getBlock(x, y, 0).getPos(), Blocks.AIR);
            }
        }
    }

    /**
     * Get the placed {@link BlockState}
     *
     * @param context The {@link BlockPlaceContext}
     * @return The placed {@link BlockState}
     */
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * Create the {@link BlockState} definition
     *
     * @param builder The {@link StateDefinition.Builder}
     */
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /**
     * Get or create the snow golem base {@link BlockPattern}
     *
     * @return The snow golem base {@link BlockPattern}
     */
    private BlockPattern getOrCreateSnowGolemBase() {
        if (this.snowGolemBase == null) {
            this.snowGolemBase = BlockPatternBuilder.start().aisle(new String[]{" ", "#", "#"}).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.snowGolemBase;
    }

    /**
     * Get or create the snow golem full {@link BlockPattern}
     *
     * @return The snow golem full {@link BlockPattern}
     */
    private BlockPattern getOrCreateSnowGolemFull() {
        if (this.snowGolemFull == null) {
            this.snowGolemFull = BlockPatternBuilder.start().aisle(new String[]{"^", "#", "#"}).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.snowGolemFull;
    }

    /**
     * Get or create the iron golem base {@link BlockPattern}
     *
     * @return The iron golem base {@link BlockPattern}
     */
    private BlockPattern getOrCreateIronGolemBase() {
        if (this.ironGolemBase == null) {
            this.ironGolemBase = BlockPatternBuilder.start().aisle(new String[]{"~ ~", "###", "~#~"}).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir)).build();
        }
        return this.ironGolemBase;
    }

    /**
     * Get or create the iron golem full {@link BlockPattern}
     *
     * @return The iron golem full {@link BlockPattern}
     */
    private BlockPattern getOrCreateIronGolemFull() {
        if (this.ironGolemFull == null) {
            this.ironGolemFull = BlockPatternBuilder.start().aisle(new String[]{"~^~", "###", "~#~"}).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState(BlockBehaviour.BlockStateBase::isAir)).build();
        }
        return this.ironGolemFull;
    }

    /**
     * Get or create the copper golem base {@link BlockPattern}
     *
     * @return The copper golem base {@link BlockPattern}
     */
    private BlockPattern getOrCreateCopperGolemBase() {
        if (this.copperGolemBase == null) {
            this.copperGolemBase = BlockPatternBuilder.start().aisle(new String[]{" ", "#"}).where('#', BlockInWorld.hasState((block) -> block.is(BlockTags.COPPER))).build();
        }
        return this.copperGolemBase;
    }

    /**
     * Get or create the copper golem full {@link BlockPattern}
     *
     * @return The copper golem full {@link BlockPattern}
     */
    private BlockPattern getOrCreateCopperGolemFull() {
        if (this.copperGolemFull == null) {
            this.copperGolemFull = BlockPatternBuilder.start().aisle(new String[]{"^", "#"}).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState((block) -> block.is(BlockTags.COPPER))).build();
        }
        return this.copperGolemFull;
    }

    /**
     * Replace the copper block used to spawn a copper golem with the appropriate copper chest
     *
     * @param level The {@link Level} reference
     * @param match The {@link BlockPattern.BlockPatternMatch}
     */
    public void replaceCopperBlockWithChest(final Level level, final BlockPattern.BlockPatternMatch match) {
        final BlockInWorld copperBlock = match.getBlock(0, 1, 0);
        level.setBlock(copperBlock.getPos(), CopperChestBlock.getFromCopperBlock(copperBlock.getState().getBlock(), match.getBlock(0, 0, 0).getState().getValue(FACING), level, copperBlock.getPos()), 2);
    }

    /**
     * Get the block's codec
     *
     * @return The block's codec
     */
    @Override
    public @NonNull MapCodec<? extends CarvedWhitePumpkinBlock> codec() {
        return CODEC;
    }
}