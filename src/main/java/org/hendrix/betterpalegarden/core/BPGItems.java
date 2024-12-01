package org.hendrix.betterpalegarden.core;

import com.google.common.base.Suppliers;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.function.Supplier;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link Item Items}
 */
public final class BPGItems {

    //#region Items

    public static final Item SNOW_GOLEM_SPAWN_EGG = registerItem("snow_golem_spawn_egg", Suppliers.memoize(() -> new SpawnEggItem(BPGEntities.SNOW_GOLEM, 14283506, 8496292, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, IdentifierUtils.modIdentifier("snow_golem_spawn_egg"))).useItemPrefixedTranslationKey())));

    //#endregion

    /**
     * Register an {@link Item Item}
     *
     * @param name The {@link String Item Name}
     * @param itemSupplier The {@link Supplier<Item> Item Supplier}
     */
    private static Item registerItem(final String name, final Supplier<Item> itemSupplier) {
        return Registry.register(Registries.ITEM, IdentifierUtils.modIdentifier(name), itemSupplier.get());
    }

    /**
     * Register all {@link Item Items}
     */
    public static void register() {

    }

}