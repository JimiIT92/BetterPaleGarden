package org.hendrix.betterpalegarden.core;

import com.google.common.base.Suppliers;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.EquippableDispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.block.CarvedWhitePumpkinBlock;
import org.hendrix.betterpalegarden.block.ThornBushBlock;
import org.hendrix.betterpalegarden.block.WaxedCreakingHeartBlock;
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
    )));

    public static final Block CARVED_WHITE_PUMPKIN = registerBlock("carved_white_pumpkin", Suppliers.memoize(() -> new CarvedWhitePumpkinBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .allowsSpawning(Blocks::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, IdentifierUtils.modIdentifier("carved_white_pumpkin")))
    )));

    public static final Block SOUL_O_LANTERN = registerBlock("soul_o_lantern", Suppliers.memoize(() -> new CarvedWhitePumpkinBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .allowsSpawning(Blocks::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .luminance(state -> 10)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, IdentifierUtils.modIdentifier("soul_o_lantern")))
    )));

    public static final Block THORN_BUSH = registerBlock("thorn_bush", Suppliers.memoize(() -> new ThornBushBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .replaceable()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, IdentifierUtils.modIdentifier("thorn_bush")))
    )));

    public static final Block WAXED_CREAKING_HEART = registerBlock("waxed_creaking_heart", Suppliers.memoize(() -> new WaxedCreakingHeartBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(5.0F)
                    .sounds(BlockSoundGroup.CREAKING_HEART)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, IdentifierUtils.modIdentifier("waxed_creaking_heart")))
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
        final Block block = registerBlockWithoutBlockItem(name, blockSupplier);
        BPGItems.registerBlockItem(IdentifierUtils.modIdentifier(name), blockSupplier);
        return block;
    }

    /**
     * Register a {@link Block Block}
     *
     * @param name The {@link String Block name}
     * @param blockSupplier The {@link Supplier<Block> Block Supplier}
     * @return The {@link Block registered Block}
     */
    private static Block registerBlockWithoutBlockItem(final String name, final Supplier<Block> blockSupplier) {
        final Identifier identifier = IdentifierUtils.modIdentifier(name);
        return Registry.register(Registries.BLOCK, identifier, blockSupplier.get());
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