package org.hendrix.betterpalegarden.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

/**
 * Mixin for the {@link WorldRenderer World Renderer class}.
 * Adds Fog to the Pale Garden
 */
@Mixin(WorldRenderer.class)
public final class FogMixin {

    @Shadow @Final private MinecraftClient client;
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
    private Fog getTerrainFog(final Fog fog) {
        final MinecraftClient client = MinecraftClient.getInstance();
        final ClientWorld world = client.world;
        final ClientPlayerEntity player = client.player;

        if (player == null || world == null) {
            return fog;
        }

        final Optional<RegistryKey<Biome>> biomeKey = world.getBiome(player.getBlockPos()).getKey();
        if(biomeKey.isPresent() && biomeKey.get().equals(IdentifierUtils.paleGardenRegistryKey())) {
            if(fogAlpha < maxFogAlpha) {
                fogAlpha += fogAlphaScaling;
            }
            return getPaleGardenFog(client);
        } else if(fogAlpha > 0F) {
            fogAlpha -= fogAlphaScaling;
            return getPaleGardenFog(client);
        }

        return fog;
    }

    /**
     * Get the {@link Fog Pale Garden Fog}
     *
     * @param client The {@link MinecraftClient Minecraft Client instance}
     * @return The {@link Fog Pale Garden Fog}
     */
    @Unique
    private Fog getPaleGardenFog(final MinecraftClient client) {
        return BackgroundRenderer.applyFog(client.gameRenderer.getCamera(), BackgroundRenderer.FogType.FOG_TERRAIN, new Vector4f(fogColor, fogColor, fogColor, fogAlpha), 24, true, client.getRenderTickCounter().getTickDelta(false));
    }

}