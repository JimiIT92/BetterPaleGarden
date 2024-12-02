package org.hendrix.betterpalegarden.core;

import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link ArmorTrimPattern Armor Trim Patterns}
 */
public final class BPGArmorTrimPatterns {

    //#region Armor Trim Patterns

    public static final RegistryKey<ArmorTrimPattern> CREAKED = RegistryKey.of(RegistryKeys.TRIM_PATTERN, IdentifierUtils.modIdentifier("creaked"));

    //#endregion

    /**
     * Register all {@link ArmorTrimPattern Armor Trim Patterns}
     *
     * @param armorTrimPatternRegisterable The {@link Registerable<ArmorTrimPattern> Armor Trim Pattern registerable}
     */
    public static void bootstrap(final Registerable<ArmorTrimPattern> armorTrimPatternRegisterable) {
        armorTrimPatternRegisterable.register(CREAKED, new ArmorTrimPattern(CREAKED.getValue(), Registries.ITEM.getEntry(BPGItems.CREAKED_ARMOR_TRIM_SMITHING_TEMPLATE), Text.translatable(Util.createTranslationKey("trim_pattern", CREAKED.getValue())), false));
    }
}