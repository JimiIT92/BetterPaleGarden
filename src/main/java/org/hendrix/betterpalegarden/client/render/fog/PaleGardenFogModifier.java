package org.hendrix.betterpalegarden.client.render.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
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
     * Get the {@link Integer Fog Color}
     *
     * @param world The {@link ClientWorld World reference}
     * @param camera The {@link Camera Camera reference}
     * @param viewDistance The {@link Integer view distance}
     * @param skyDarkness The {@link Float sky darkness value}
     * @return The {@link Integer -12171705}
     */
    public int getFogColor(final ClientWorld world, final Camera camera, final int viewDistance, final float skyDarkness) {
        return -12171705;
    }

    /**
     * Apply the fog
     *
     * @param data The {@link FogData Fog data}
     * @param cameraEntity The {@link Entity Entity that the Fog should be applied to}
     * @param cameraPos The {@link BlockPos Camera Block Pos}
     * @param world The {@link ClientWorld World reference}
     * @param viewDistance The {@link Float view distance}
     * @param tickCounter The {@link RenderTickCounter tick counter}
     */
    public void applyStartEndModifier(final FogData data, final Entity cameraEntity, final BlockPos cameraPos, final ClientWorld world, final float viewDistance, final RenderTickCounter tickCounter) {
        if (cameraEntity instanceof LivingEntity) {
            final float fogThickness = (7.0F * BetterPaleGarden.MAX_FOG_THICKNESS) / BetterPaleGarden.config().FOG_THICKNESS;
            data.environmentalStart = fogThickness * 0.25F;
            data.environmentalEnd = fogThickness;
            data.skyEnd = fogThickness * 0.8F;
            data.cloudEnd = fogThickness * 0.8F;
        }
    }

    /**
     * Check if the Fog should be applied
     *
     * @param submersionType The {@link CameraSubmersionType Camera Submersion Type}
     * @param cameraEntity The {@link Entity Entity that the Fog should be applied to}
     * @return {@link Boolean True if the Fog should be applied}
     */
    public boolean shouldApply(final @Nullable CameraSubmersionType submersionType, final Entity cameraEntity) {
        return submersionType == CameraSubmersionType.ATMOSPHERIC && BiomeUtils.isInPaleGarden(cameraEntity.getWorld(), cameraEntity.getBlockPos());
    }

}