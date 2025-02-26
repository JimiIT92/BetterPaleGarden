package org.hendrix.betterpalegarden.core;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.Optional;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link PaintingVariant Painting Variants}
 */
public final class BPGPaintingVariants {

    //#region Painting Variants

    public static final RegistryKey<PaintingVariant> PALE = RegistryKey.of(RegistryKeys.PAINTING_VARIANT, IdentifierUtils.modIdentifier("pale"));

    //#endregion

    /**
     * Register all {@link PaintingVariant Painting Variants}
     *
     * @param paintingRegisterable The {@link Registerable<PaintingVariant> Painting Variant registerable}
     */
    public static void bootstrap(final Registerable<PaintingVariant> paintingRegisterable) {
        paintingRegisterable.register(PALE, new PaintingVariant(
                2,
                2,
                PALE.getValue(),
                Optional.of(Text.translatable(PALE.getValue().toTranslationKey("painting", "title")).formatted(Formatting.YELLOW)),
                Optional.empty()
        ));
    }

    /**
     * Register all {@link PaintingVariant Painting Variants}
     */
    public static void register() {

    }

}