package org.hendrix.betterpalegarden.core;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.function.Function;

/**
 * {@link BetterPaleGarden} {@link Item Items}
 */
public final class BPGItems {

    //#region Items

    public static final Item PUMPKIN_SOUP = register("pumpkin_soup", Item::new, new Item.Properties().stacksTo(1).food(new FoodProperties(8, 0.6F, false)).usingConvertsTo(Items.BOWL));
    public static final Item CREAKED_ARMOR_TRIM_SMITHING_TEMPLATE = register("creaked_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, new Item.Properties().rarity(Rarity.UNCOMMON));

    //#endregion

    /**
     * Register an {@link Item}
     *
     * @param name The item name
     * @param itemFactory The item factory
     * @param properties The {@link Item.Properties item properties}
     * @return The registered {@link Item}
     * @param <T> The item type
     */
    public static <T extends Item> T register(final String name, final Function<Item.Properties, T> itemFactory, final Item.Properties properties) {
        final ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, IdentifierUtils.modded(name));
        final T item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    /**
     * Register all {@link Item items}
     */
    public static void register() {

    }

}