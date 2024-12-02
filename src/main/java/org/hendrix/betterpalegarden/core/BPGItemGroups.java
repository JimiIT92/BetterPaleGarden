package org.hendrix.betterpalegarden.core;

import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link ItemGroup Item Groups}
 */
public final class BPGItemGroups {

    //#region Item Groups

    public static final ItemGroup BETTER_PALE_GARDEN = Registry.register(
            Registries.ITEM_GROUP,
            IdentifierUtils.modIdentifier(BetterPaleGarden.MOD_ID),
            FabricItemGroup.builder()
                    .icon(Suppliers.memoize(() -> new ItemStack(BPGBlocks.CARVED_WHITE_PUMPKIN)))
                    .displayName(Text.translatable("itemgroup." + BetterPaleGarden.MOD_ID + "." + BetterPaleGarden.MOD_ID))
                    .entries((displayContext, entries) -> {
                        entries.add(BPGBlocks.WHITE_PUMPKIN);
                        entries.add(BPGBlocks.CARVED_WHITE_PUMPKIN);
                        entries.add(BPGBlocks.SOUL_O_LANTERN);
                        entries.add(BPGBlocks.THORN_BUSH);
                        entries.add(BPGBlocks.WAXED_CREAKING_HEART);
                        entries.add(BPGBlocks.CRACKED_RESIN_BRICKS);
                        entries.add(BPGBlocks.MOSSY_RESIN_BRICKS);
                        entries.add(BPGItems.SNOW_GOLEM_SPAWN_EGG);
                    })
                    .build()
    );

    //#endregion

    /**
     * Register all {@link ItemGroup Item Groups}
     */
    public static void register() {

    }

}