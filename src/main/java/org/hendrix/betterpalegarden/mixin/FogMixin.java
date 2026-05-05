package org.hendrix.betterpalegarden.mixin;

import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import org.hendrix.betterpalegarden.client.renderer.fog.PaleGardenFogEnvironment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Mixin for the {@link FogRenderer} class
 */
@Mixin(FogRenderer.class)
public final class FogMixin {

    /**
     * The {@link List<FogEnvironment>}
     */
    @Shadow @Final private static List<FogEnvironment> FOG_ENVIRONMENTS;

    /**
     * Add the {@link PaleGardenFogEnvironment} to the Pale Garden biome
     *
     * @param callbackInfo The {@link CallbackInfo}
     */
    @Inject(at = @At(value = "RETURN"), method = "<init>")
    private void init(final CallbackInfo callbackInfo) {
        FOG_ENVIRONMENTS.addFirst(new PaleGardenFogEnvironment());
    }

}