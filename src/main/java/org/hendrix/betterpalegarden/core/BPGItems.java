package org.hendrix.betterpalegarden.core;

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.function.Supplier;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link Item Items}
 */
public final class BPGItems {

    //#region Items

    public static final Item SNOW_GOLEM_SPAWN_EGG = registerItem("snow_golem_spawn_egg", Suppliers.memoize(() -> new SpawnEggItem(BPGEntities.SNOW_GOLEM, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, IdentifierUtils.modIdentifier("snow_golem_spawn_egg"))).useItemPrefixedTranslationKey())));
    public static final Item CREAKED_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("creaked_armor_trim_smithing_template", Suppliers.memoize(() -> SmithingTemplateItem.of(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, IdentifierUtils.modIdentifier("creaked_armor_trim_smithing_template"))))));

    //#endregion

    /**
     * Register a {@link BlockItem Block Item}
     *
     * @param identifier The {@link Identifier Block Identifier}
     * @param blockSupplier The {@link Supplier<Block> Block Supplier}
     */
    public static void registerBlockItem(final Identifier identifier, final Supplier<Block> blockSupplier) {
        Item.Settings itemSettings = new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)).useBlockPrefixedTranslationKey();
        if(identifier.getPath().equals("carved_white_pumpkin")) {
            itemSettings = itemSettings.component(
                    DataComponentTypes.EQUIPPABLE,
                    EquippableComponent.builder(EquipmentSlot.HEAD).swappable(false).cameraOverlay(Identifier.ofVanilla("misc/pumpkinblur")).build()
            );
        } else if(identifier.getPath().equals("glowing_pumpkin")) {
            itemSettings = itemSettings.component(
                    DataComponentTypes.EQUIPPABLE,
                    EquippableComponent.builder(EquipmentSlot.HEAD).swappable(false).cameraOverlay(IdentifierUtils.modIdentifier("misc/glowingpumpkinblur")).build()
            );
        }

        Registry.register(Registries.ITEM, identifier, new BlockItem(blockSupplier.get(), itemSettings));
    }

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