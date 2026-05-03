package org.hendrix.betterpalegarden.core;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.Arrays;

/**
 * {@link BetterPaleGarden} {@link CreativeModeTab Creative Mode Tabs}
 */
public final class BPGCreativeModeTabs {

    //#region Creative Mode Tabs

    public static final CreativeModeTab BETTER_PALE_GARDEN = register(
            BetterPaleGarden.MOD_ID,
            FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(BPGBlocks.CARVED_WHITE_PUMPKIN))
                    .title(Component.translatable("creativeTab." + BetterPaleGarden.MOD_ID + "." + BetterPaleGarden.MOD_ID))
                    .displayItems((params, output) -> {
                        addContent(
                                output,
                                BPGBlocks.WHITE_PUMPKIN,
                                BPGBlocks.CARVED_WHITE_PUMPKIN,
                                BPGBlocks.SOUL_O_LANTERN,
                                BPGBlocks.GLOWING_PUMPKIN,
                                BPGBlocks.THORN_BUSH,
                                BPGBlocks.CHRYSANTHEMUM,
                                BPGBlocks.WAXED_CREAKING_HEART,
                                BPGBlocks.CRACKED_RESIN_BRICKS,
                                BPGBlocks.MOSSY_RESIN_BRICKS,
                                BPGItems.CREAKED_ARMOR_TRIM_SMITHING_TEMPLATE,
                                BPGItems.PUMPKIN_SOUP,
                                BPGItems.SNOW_GOLEM_SPAWN_EGG
                        );
                        addModdedPaintings(params, output);
                    })
                    .build()
    );

    //#endregion

    /**
     * Add some content to a creative mode tab
     *
     * @param output The {@link CreativeModeTab.Output}
     * @param content The {@link ItemLike content to add}
     */
    private static void addContent(final CreativeModeTab.Output output, final ItemLike... content) {
        Arrays.stream(content).forEach(output::accept);
    }

    /**
     * Add modded paintings to the creative mode tab
     *
     * @param itemDisplayParameters The {@link CreativeModeTab.ItemDisplayParameters}
     * @param output The {@link CreativeModeTab.Output}
     */
    private static void addModdedPaintings(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output output) {
        itemDisplayParameters.holders().lookup(Registries.PAINTING_VARIANT).flatMap(instruments -> instruments.get(PaintingVariantTags.PLACEABLE)).ifPresent((tag) -> tag
                .stream()
                .map(paintingVariantHolder -> {
                    ItemStack stack = new ItemStack(Items.PAINTING);
                    stack.set(DataComponents.PAINTING_VARIANT, paintingVariantHolder);
                    return stack;
                })
                .filter(BPGCreativeModeTabs::isModdedPainting)
                .forEach((stack) -> output.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)));
    }

    /**
     * Check whether a painting variant is modded or not
     *
     * @param itemStack The {@link ItemStack to check}
     * @return {@link Boolean True} if is a modded painting variant
     */
    private static boolean isModdedPainting(final ItemStack itemStack) {
        final Holder<PaintingVariant> paintingVariantHolder = itemStack.get(DataComponents.PAINTING_VARIANT);
        if(paintingVariantHolder != null) {
            return paintingVariantHolder.is(key -> key.identifier().getNamespace().equalsIgnoreCase(BetterPaleGarden.MOD_ID));
        }
        return false;
    }

    /**
     * Register a {@link CreativeModeTab}
     *
     * @param name The creative mode tab name
     * @param creativeModeTab The {@link CreativeModeTab to register}
     * @return The registered {@link CreativeModeTab}
     */
    private static CreativeModeTab register(final String name, final CreativeModeTab creativeModeTab) {
        final ResourceKey<CreativeModeTab> resourceKey = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), IdentifierUtils.modded(name));
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, resourceKey, creativeModeTab);
    }

    /**
     * Register all creative mode tabs
     */
    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
            output.getSearchTabStacks().removeIf(BPGCreativeModeTabs::isModdedPainting);
            output.getDisplayStacks().removeIf(BPGCreativeModeTabs::isModdedPainting);
        });
    }

}