package org.hendrix.betterpalegarden.core;

import com.google.common.base.Suppliers;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.EquippableDispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.block.CarvedWhitePumpkinBlock;
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
    public static final Block CARVED_WHITE_PUMPKIN = registerBlock("carved_white_pumpkin", Suppliers.memoize(() -> new CarvedWhitePumpkinBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .allowsSpawning(Blocks::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, IdentifierUtils.modIdentifier("carved_white_pumpkin")))
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
        Item.Settings itemSettings = new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)).useBlockPrefixedTranslationKey();
        if(identifier.getPath().equals("carved_white_pumpkin")) {
            itemSettings = itemSettings.component(
                    DataComponentTypes.EQUIPPABLE,
                    EquippableComponent.builder(EquipmentSlot.HEAD).swappable(false).cameraOverlay(Identifier.ofVanilla("misc/pumpkinblur")).build()
            );
        }

        Registry.register(Registries.ITEM, identifier, new BlockItem(blockSupplier.get(), itemSettings));
    }

    /**
     * Register all {@link Block Blocks}
     */
    public static void register() {
        registerDispenseBehaviors();
    }

    /**
     * Register {@link DispenserBlock Dispenser Block Behaviors}
     */
    public static void registerDispenseBehaviors() {
        DispenserBlock.registerBehavior(CARVED_WHITE_PUMPKIN, new FallibleItemDispenserBehavior() {
            /**
             * Try spawning a golem from a {@link DispenserBlock Dispenser Block}
             *
             * @param pointer The {@link BlockPointer Block Pointer reference}
             * @param stack The {@link ItemStack Item Stack} inside the {@link DispenserBlock Dispenser Block}
             * @return The {@link ItemStack modified Item Stack}
             */
            @Override
            protected ItemStack dispenseSilently(final BlockPointer pointer, final ItemStack stack) {
                final World world = pointer.world();
                final BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                final CarvedWhitePumpkinBlock carvedWhitePumpkinBlock = (CarvedWhitePumpkinBlock)CARVED_WHITE_PUMPKIN;
                if (world.isAir(blockPos) && carvedWhitePumpkinBlock.canDispense(world, blockPos)) {
                    if (!world.isClient) {
                        world.setBlockState(blockPos, carvedWhitePumpkinBlock.getDefaultState(), Block.NOTIFY_ALL);
                        world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                    }
                    stack.decrement(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(EquippableDispenserBehavior.dispense(pointer, stack));
                }
                return stack;
            }
        });
    }

}