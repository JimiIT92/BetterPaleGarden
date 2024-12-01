package org.hendrix.betterpalegarden.core;

import com.google.common.base.Suppliers;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.block.WhitePumpkinBlock;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

import java.util.function.Supplier;

/**
 * {@link BetterPaleGarden Better Pale Garden} {@link Block Blocks}
 */
public final class BPGBlocks {

    //#region Blocks

    public static final Block WHITE_PUMPKIN = registerBlock("white_pumpkin", Suppliers.memoize(() -> new WhitePumpkinBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .instrument(NoteBlockInstrument.DIDGERIDOO)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, IdentifierUtils.modIdentifier("white_pumpkin")))
                    .requires(FeatureFlags.WINTER_DROP)
    )));

    //#endregion

    /**
     * Register a {@link Block Block}
     *
     * @param name The {@link String Block name}
     * @param blockSupplier The {@link Supplier<Block> Block Supplier}
     * @return The {@link Block registered Block}
     */
    private static Block registerBlock(final String name, final Supplier<Block> blockSupplier) {
        final Identifier identifier = IdentifierUtils.modIdentifier(name);
        registerBlockItem(identifier, blockSupplier);
        return Registry.register(Registries.BLOCK, identifier, blockSupplier.get());
    }

    /**
     * Register a {@link BlockItem Block Item}
     *
     * @param identifier The {@link Identifier Block Identifier}
     * @param blockSupplier The {@link Supplier<Block> Block Supplier}
     */
    private static void registerBlockItem(final Identifier identifier, final Supplier<Block> blockSupplier) {
        Registry.register(Registries.ITEM, identifier, new BlockItem(blockSupplier.get(), new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)).useBlockPrefixedTranslationKey().modelId(identifier)));
    }

    /**
     * Register all {@link Block Blocks}
     */
    public static void register() {

    }

}