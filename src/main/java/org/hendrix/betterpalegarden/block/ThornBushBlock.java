package org.hendrix.betterpalegarden.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.hendrix.betterpalegarden.core.BPGDamageTypes;

/**
 * Implementation class for a {@link TallPlantBlock Thorn Bush Block}
 */
public final class ThornBushBlock extends TallFlowerBlock {

    /**
     * The {@link Float minimum movement range} for an {@link Entity Entity} to take damage
     */
    private static final float MIN_MOVEMENT_FOR_DAMAGE = 0.003F;

    /**
     * Constructor. Set the {@link Block Block} properties
     *
     * @param settings The {@link Block Block} properties
     */
    public ThornBushBlock(final Settings settings) {
        super(settings);
    }

    /**
     * Damage an {@link Entity Entity} when colliding with the {@link Block Block}
     *
     * @param state The {@link BlockState current Block State}
     * @param world The {@link World World reference}
     * @param pos The {@link BlockPos current Block Pos}
     * @param entity The {@link Entity Entity colliding}
     * @param handler The {@link EntityCollisionHandler Entity collision handler}
     * @param simulate {@link Boolean Whether the damage should be simulated}
     */
    @Override
    protected void onEntityCollision(final BlockState state, final World world, final BlockPos pos, final Entity entity, final EntityCollisionHandler handler, final boolean simulate) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.8F, 0.75, 0.8F));
            if(world instanceof ServerWorld serverWorld) {
                final Vec3d movementDistance = entity.isControlledByPlayer() ? entity.getMovement() : entity.getLastRenderPos().subtract(entity.getEntityPos());
                if (movementDistance.horizontalLengthSquared() > 0.0) {
                    if (Math.abs(movementDistance.getX()) >= MIN_MOVEMENT_FOR_DAMAGE || Math.abs(movementDistance.getZ()) >= MIN_MOVEMENT_FOR_DAMAGE) {
                        entity.damage(serverWorld, BPGDamageTypes.of(serverWorld, BPGDamageTypes.THORN_BUSH), 1.0F);
                    }
                }
            }
        }
    }

}