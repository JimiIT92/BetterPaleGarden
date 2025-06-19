package org.hendrix.betterpalegarden.mixin;

import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import org.hendrix.betterpalegarden.client.render.fog.PaleGardenFogModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Mixin for the {@link FogRenderer Fog Renderer class}.
 * Adds Fog to the Pale Garden
 */
@Mixin(FogRenderer.class)
public abstract class FogMixin {

    /**
     * The {@link List<FogModifier> Fog Modifiers}
     */
    @Shadow @Final private static List<FogModifier> FOG_MODIFIERS;

    /**
     * Add the {@link PaleGardenFogModifier Pale Garden Fog Modifier} to the {@link #FOG_MODIFIERS Fog Modifiers}
     *
     * @param callbackInfo The {@link CallbackInfo Callback Info}
     */
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(final CallbackInfo callbackInfo) {
        FOG_MODIFIERS.addFirst(new PaleGardenFogModifier());
    }

}