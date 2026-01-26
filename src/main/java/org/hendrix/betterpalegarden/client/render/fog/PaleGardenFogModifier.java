package org.hendrix.betterpalegarden.client.render.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.WorldEnvironmentAttributeAccess;
import net.minecraft.world.biome.BiomeKeys;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.BiomeUtils;
import org.jetbrains.annotations.Nullable;

/**
 * {@link FogModifier Fog Modifier} for the {@link BiomeKeys#PALE_GARDEN Pale Garden Biome}
 */
@Environment(EnvType.CLIENT)
public final class PaleGardenFogModifier extends FogModifier {

    /**
     * The {@link Float maximum fog thickness}
     */
    private final Float MAX_FOG_THICKNESS = (7.0F * BetterPaleGarden.MAX_FOG_THICKNESS) / BetterPaleGarden.config().FOG_THICKNESS;

    /**
     * How many {@link Integer ticks} the {@link ClientPlayerEntity player} has been inside the Pale Garden
     */
    private float ticksInsidePaleGarden = 0.0F;
    /**
     * The {@link Integer maximum amount of thicks} for Fog to reach its maximum thickness
     */
    private final float maxTicksForFogThickness = 200.0F;

    /**
     * Get the {@link Integer Fog Color}
     *
     * @param world The {@link ClientWorld World reference}
     * @param camera The {@link Camera Camera reference}
     * @param viewDistance The {@link Integer view distance}
     * @param skyDarkness The {@link Float sky darkness value}
     * @return The {@link Integer -12171705}
     */
    public int getFogColor(final ClientWorld world, final Camera camera, final int viewDistance, final float skyDarkness) {
        return this.isInPaleGarden(camera.getFocusedEntity()) || this.ticksInsidePaleGarden > 0 ? -12171705 : world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.FOG_COLOR_VISUAL);
    }

    /**
     * Apply the fog
     *
     * @param data The {@link FogData Fog data}
     * @param camera The {@link Camera entity camera instance}
     * @param world The {@link ClientWorld World reference}
     * @param viewDistance The {@link Float view distance}
     * @param tickCounter The {@link RenderTickCounter tick counter}
     */
    public void applyStartEndModifier(final FogData data, final Camera camera, final ClientWorld world, final float viewDistance, final RenderTickCounter tickCounter) {
        if (camera.getFocusedEntity() instanceof LivingEntity cameraEntity) {
            final boolean isInPaleGarden = this.isInPaleGarden(cameraEntity);
            if(isInPaleGarden || this.ticksInsidePaleGarden > 0) {
                if(isInPaleGarden) {
                    this.ticksInsidePaleGarden = Math.min(this.ticksInsidePaleGarden + 1, this.maxTicksForFogThickness);
                } else {
                    this.ticksInsidePaleGarden--;
                }
                final float fogThickness = MAX_FOG_THICKNESS * this.getFogThicknessMultiplier();
                data.environmentalStart = fogThickness * 0.25F;
                data.environmentalEnd = fogThickness;
                data.skyEnd = fogThickness * 0.8F;
                data.cloudEnd = fogThickness * 0.8F;
            } else {
                final WorldEnvironmentAttributeAccess environmentAttributes = world.getEnvironmentAttributes();
                final float fogStart = environmentAttributes.getAttributeValue(EnvironmentAttributes.FOG_START_DISTANCE_VISUAL);
                final float fogEnd = environmentAttributes.getAttributeValue(EnvironmentAttributes.FOG_END_DISTANCE_VISUAL);
                final float skyEnd = environmentAttributes.getAttributeValue(EnvironmentAttributes.SKY_FOG_END_DISTANCE_VISUAL);
                final float cloudEnd = environmentAttributes.getAttributeValue(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE_VISUAL);
                if(data.environmentalStart != fogStart) {
                    data.environmentalStart = fogStart;
                }
                if(data.environmentalEnd != fogEnd) {
                    data.environmentalEnd = fogEnd;
                }
                if(data.skyEnd != skyEnd) {
                    data.skyEnd = skyEnd;
                }
                if(data.cloudEnd != cloudEnd) {
                    data.cloudEnd = cloudEnd;
                }
            }
        }
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
     * Check if the Fog should be applied
     *
     * @param submersionType The {@link CameraSubmersionType Camera Submersion Type}
     * @param cameraEntity The {@link Entity Entity that the Fog should be applied to}
     * @return {@link Boolean True if the Fog should be applied}
     */
    public boolean shouldApply(final @Nullable CameraSubmersionType submersionType, final Entity cameraEntity) {
        return submersionType == CameraSubmersionType.ATMOSPHERIC && BetterPaleGarden.config().ENABLE_FOG;
    }

    /**
     * Check whether an {@link Entity entity} is inside the Pale Garden
     *
     * @param entity The {@link Entity entity} to check
     * @return {@link Boolean True if is inside the Pale Garden}
     */
    private boolean isInPaleGarden(final Entity entity) {
        return BiomeUtils.isInPaleGarden(entity.getEntityWorld(), entity.getBlockPos());
    }

}