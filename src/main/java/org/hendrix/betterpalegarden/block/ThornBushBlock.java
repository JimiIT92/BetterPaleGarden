package org.hendrix.betterpalegarden.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.hendrix.betterpalegarden.core.BPGDamageTypes;
import org.jspecify.annotations.NonNull;

/**
 * Implementation class for a thorn bush block
 */
public final class ThornBushBlock extends TallFlowerBlock {

    /**
     * The minimum speed that an entity must have to be damaged by the block
     */
    private static final float HURT_SPEED_THRESHOLD = 0.003F;

    /**
     * Constructor. Set the {@link BlockBehaviour.Properties}
     *
     * @param properties The {@link BlockBehaviour.Properties}
     */
    public ThornBushBlock(final Properties properties) {
        super(properties);
    }

    /**
     * Damage an entity when is inside the block
     *
     * @param state The current {@link BlockState}
     * @param level The {@link Level} reference
     * @param pos The current {@link BlockPos}
     * @param entity The {@link Entity} that is inside the block
     * @param effectApplier The {@link InsideBlockEffectApplier}
     * @param isPrecise Whether the collision should be a precise check
     */
    protected void entityInside(final @NonNull BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final @NonNull Entity entity, final @NonNull InsideBlockEffectApplier effectApplier, final boolean isPrecise) {
        if (entity instanceof LivingEntity) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
            if (level instanceof ServerLevel serverLevel) {
                final Vec3 movement = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());
                if (movement.horizontalDistanceSqr() > (double)0.0F) {
                    if (Math.abs(movement.x()) >= HURT_SPEED_THRESHOLD || Math.abs(movement.z()) >= HURT_SPEED_THRESHOLD) {
                        entity.hurtServer(serverLevel, level.damageSources().source(BPGDamageTypes.THORN_BUSH), 1.0F);
                    }
                }
            }
        }
    }

}