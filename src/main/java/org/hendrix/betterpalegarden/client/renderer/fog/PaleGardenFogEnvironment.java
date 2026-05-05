package org.hendrix.betterpalegarden.client.renderer.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.material.FogType;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * {@link FogEnvironment} for the Pale Garden Biome
 */
@Environment(EnvType.CLIENT)
public final class PaleGardenFogEnvironment extends FogEnvironment {

    /**
     * The {@link Float maximum fog thickness}
     */
    private final Float MAX_FOG_THICKNESS = (7.0F * BetterPaleGarden.MAX_FOG_THICKNESS) / BetterPaleGarden.config().FOG_THICKNESS;

    /**
     * How many {@link Integer ticks} the player has been inside the Pale Garden
     */
    private float ticksInsidePaleGarden = 0.0F;
    /**
     * The {@link Integer maximum amount of thicks} for Fog to reach its maximum thickness
     */
    private final float maxTicksForFogThickness = 200.0F;

    /**
     * Get the fog color
     *
     * @param level The {@link ClientLevel} reference
     * @param camera The {@link Camera} reference
     * @param renderDistance The current render distance
     * @param partialTicks The level partial ticks
     * @return The fog color
     */
    @Override
    public int getBaseColor(final @NonNull ClientLevel level, final Camera camera, final int renderDistance, final float partialTicks) {
        return this.isInPaleGarden(camera.entity()) || this.ticksInsidePaleGarden > 0 ? -12171705 : level.environmentAttributes().getValue(EnvironmentAttributes.FOG_COLOR, camera.blockPosition());
    }

    /**
     * Apply the fog
     *
     * @param fog The {@link FogData}
     * @param camera The {@link Camera} reference
     * @param level The {@link ClientLevel} reference
     * @param renderDistance The current render distance
     * @param deltaTracker The {@link DeltaTracker} reference
     */
    @Override
    public void setupFog(final @NonNull FogData fog, final Camera camera, final @NonNull ClientLevel level, final float renderDistance, final @NonNull DeltaTracker deltaTracker) {
        if (camera.entity() instanceof LivingEntity cameraEntity) {
            final boolean isInPaleGarden = this.isInPaleGarden(cameraEntity);
            float fogStart, fogEnd, skyEnd, cloudEnd;
            if(isInPaleGarden || this.ticksInsidePaleGarden > 0) {
                if(isInPaleGarden) {
                    this.ticksInsidePaleGarden = Math.min(this.ticksInsidePaleGarden + 1, this.maxTicksForFogThickness);
                } else {
                    this.ticksInsidePaleGarden--;
                }
                final float fogThickness = MAX_FOG_THICKNESS * this.getFogThicknessMultiplier();
                fogStart = fogThickness * 0.25F;
                fogEnd = fogThickness;
                skyEnd = fogThickness * 0.8F;
                cloudEnd = fogThickness * 0.8F;
            } else {
                final EnvironmentAttributeSystem environmentAttributes = level.environmentAttributes();
                final BlockPos cameraPos = camera.blockPosition();
                fogStart = environmentAttributes.getValue(EnvironmentAttributes.FOG_START_DISTANCE, cameraPos);
                fogEnd = environmentAttributes.getValue(EnvironmentAttributes.FOG_END_DISTANCE, cameraPos);
                skyEnd = environmentAttributes.getValue(EnvironmentAttributes.SKY_FOG_END_DISTANCE, cameraPos);
                cloudEnd = environmentAttributes.getValue(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE, cameraPos);
            }
            if(fog.environmentalStart != fogStart) {
                fog.environmentalStart = fogStart;
            }
            if(fog.environmentalEnd != fogEnd) {
                fog.environmentalEnd = fogEnd;
            }
            if(fog.skyEnd != skyEnd) {
                fog.skyEnd = skyEnd;
            }
            if(fog.cloudEnd != cloudEnd) {
                fog.cloudEnd = cloudEnd;
            }
        }
    }

    /**
     * Check whether the Fog should be applied
     *
     * @param fogType The {@link FogType}
     * @param entity The {@link Entity} reference
     * @return {@link Boolean True} if the Fog should be applied
     */
    @Override
    public boolean isApplicable(final @Nullable FogType fogType, final @NonNull Entity entity) {
        return fogType == FogType.ATMOSPHERIC && BetterPaleGarden.config().ENABLE_FOG;
    }

    /**
     * Get the {@link Float fog thickness multiplier} based on the current pale garden ticks
     *
     * @return The {@link Float fog thickness multiplier}
     */
    private float getFogThicknessMultiplier() {
        return Math.max(1, 5 - 4 * (this.ticksInsidePaleGarden / this.maxTicksForFogThickness));
    }

    /**
     * Check whether an {@link Entity} is inside the Pale Garden
     *
     * @param entity The {@link Entity} reference
     * @return {@link Boolean True} if the entity is inside the Pale Garden
     */
    private boolean isInPaleGarden(final Entity entity) {
        return entity != null && entity.level().getBiome(entity.blockPosition()).is(Biomes.PALE_GARDEN);
    }

}
