package org.hendrix.betterpalegarden.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.biome.Biome;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.BiomeUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

/**
 * Mixin for the {@link WorldRenderer World Renderer class}.
 * Adds Fog to the Pale Garden
 */
@Mixin(WorldRenderer.class)
public final class FogMixin {

    /**
     * The {@link MinecraftClient Minecraft Client instance}
     */
    @Shadow @Final private MinecraftClient client;
    /**
     * The {@link ClientWorld Client World reference}
     */
    @Shadow @Nullable private ClientWorld world;
    /**
     * The current {@link Float Fog alpha}
     */
    @Unique
    private float fogAlpha;
    /**
     * The maximum {@link Float Fog alpha}
     */
    @Unique
    private static final float maxFogAlpha = 0.975F;
    /**
     * The {@link Float Fog alpha scaling}, for the dissolve effect
     */
    @Unique
    private static final float fogAlphaScaling = 0.005F;
    /**
     * The {@link Float Fog Color}
     */
    @Unique
    private static final float fogColor = 75.0F / 255.0F;

    /**
     * Apply the Fog to the {@link Biome Pale Garden Biome}
     *
     * @param fog The {@link Fog Biome Fog}
     * @return The {@link Fog modified Biome Fog}
     */
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;Lorg/joml/Vector4f;FZF)Lnet/minecraft/client/render/Fog;", ordinal = 0))
    private Fog applyFog(final Fog fog) {
        if(BetterPaleGarden.isClothConfigInstalled() && !BetterPaleGarden.config().ENABLE_FOG) {
            return fog;
        }

        final MinecraftClient client = MinecraftClient.getInstance();
        final ClientWorld world = client.world;
        final ClientPlayerEntity player = client.player;

        if (player == null || world == null) {
            return fog;
        }

        if(BiomeUtils.isInPaleGarden(world, player.getBlockPos())) {
            if(fogAlpha < maxFogAlpha) {
                fogAlpha += fogAlphaScaling;
            }
            return getPaleGardenFog(client, fog);
        } else if(fogAlpha > 0F) {
            fogAlpha -= fogAlphaScaling;
            return getPaleGardenFog(client, fog);
        }

        return fog;
    }

    /**
     * Get the {@link Fog Pale Garden Fog}
     *
     * @param client The {@link MinecraftClient Minecraft Client instance}
     * @param fog The {@link Fog original Fog}
     * @return The {@link Fog Pale Garden Fog}
     */
    @Unique
    private Fog getPaleGardenFog(final MinecraftClient client, final Fog fog) {
        return BackgroundRenderer.applyFog(
                client.gameRenderer.getCamera(),
                BackgroundRenderer.FogType.FOG_TERRAIN,
                Objects.requireNonNull(world).isNight() ? new Vector4f(fog.red(), fog.green(), fog.blue(), fogAlpha) : new Vector4f(fogColor, fogColor, fogColor, fogAlpha),
                BetterPaleGarden.MAX_FOG_THICKNESS - (BetterPaleGarden.isClothConfigInstalled() ? BetterPaleGarden.config().FOG_THICKNESS : BetterPaleGarden.DEFAULT_FOG_THICKNESS),
                true,
                client.getRenderTickCounter().getTickDelta(false)
        );
    }

}