package org.hendrix.betterpalegarden.core;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.impl.content.registry.OxidizableBlocksRegistryImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.EquipmentDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import org.hendrix.betterpalegarden.BetterPaleGarden;
import org.hendrix.betterpalegarden.block.CarvedWhitePumpkinBlock;
import org.hendrix.betterpalegarden.block.ThornBushBlock;
import org.hendrix.betterpalegarden.block.WaxedCreakingHeartBlock;
import org.hendrix.betterpalegarden.block.WhitePumpkinBlock;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.function.Function;

/**
 * {@link BetterPaleGarden} {@link Block Blocks}
 */
public final class BPGBlocks {

    //#region Blocks

    public static final Block WHITE_PUMPKIN = register(
            "white_pumpkin",
            WhitePumpkinBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN)
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
    );
    public static final Block CARVED_WHITE_PUMPKIN = register(
            "carved_white_pumpkin",
            CarvedWhitePumpkinBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CARVED_PUMPKIN)
                    .mapColor(MapColor.TERRACOTTA_WHITE),
            Pair.of(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                    .setSwappable(false)
                    .setCameraOverlay(Identifier.withDefaultNamespace("misc/pumpkinblur"))
                    .build()
            )
    );
    public static final Block SOUL_O_LANTERN = register(
            "soul_o_lantern",
            CarvedWhitePumpkinBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.JACK_O_LANTERN)
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .lightLevel(_ -> 10)
    );

    public static final Block THORN_BUSH = register(
            "thorn_bush",
            ThornBushBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)
    );

    public static final Block WAXED_CREAKING_HEART = register(
            "waxed_creaking_heart",
            WaxedCreakingHeartBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CREAKING_HEART)
    );

    //#endregion

    /**
     * Register a {@link Block} without registering a {@link BlockItem}
     *
     * @param name The block name
     * @param blockFactory The block factory
     * @param properties The {@link BlockBehaviour.Properties block properties}
     * @return The registered {@link Block}
     */
    private static Block registerBlockWithoutBlockItem(final String name, final Function<BlockBehaviour.Properties, Block> blockFactory, final BlockBehaviour.Properties properties) {
        final ResourceKey<Block> blockResourceKey = ResourceKey.create(Registries.BLOCK, IdentifierUtils.modded(name));
        final Block block = blockFactory.apply(properties.setId(blockResourceKey));
        return Registry.register(BuiltInRegistries.BLOCK, blockResourceKey, block);
    }

    /**
     * Register a {@link Block}
     *
     * @param name The block name
     * @param blockFactory The block factory
     * @param properties The {@link BlockBehaviour.Properties block properties}
     * @param itemComponents Additional components to apply to the block item
     * @return The registered {@link Block}
     * @param <T> The data component type
     */
    @SafeVarargs
    private static <T> Block register(final String name, final Function<BlockBehaviour.Properties, Block> blockFactory, final BlockBehaviour.Properties properties, final Pair<DataComponentType<T>, T>... itemComponents) {
        final Block block = registerBlockWithoutBlockItem(name, blockFactory, properties);
        final ResourceKey<Item> blockItemResourceKey = ResourceKey.create(Registries.ITEM, IdentifierUtils.modded(name));
        Item.Properties itemProperties = new Item.Properties().setId(blockItemResourceKey).useBlockDescriptionPrefix();
        if(itemComponents != null && itemComponents.length > 0) {
            Arrays.stream(itemComponents).forEach(x -> itemProperties.component(x.getFirst(), x.getSecond()));
        }
        final BlockItem blockItem = new BlockItem(block, itemProperties);
        Registry.register(BuiltInRegistries.ITEM, blockItemResourceKey, blockItem);
        return block;
    }

    /**
     * Register compostable blocks
     */
    private static void registerCompostableBlocks() {
        CompostableRegistry.INSTANCE.add(WHITE_PUMPKIN, 0.65F);
        CompostableRegistry.INSTANCE.add(CARVED_WHITE_PUMPKIN, 0.65F);
    }

    /**
     * Register dispense behaviors
     */
    private static void registerDispenseBehaviors() {
        DispenserBlock.registerBehavior(CARVED_WHITE_PUMPKIN, new OptionalDispenseItemBehavior() {
            protected @NonNull ItemStack execute(final @NonNull BlockSource source, final @NonNull ItemStack dispensed) {
                final Level level = source.level();
                final BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                CarvedWhitePumpkinBlock pumpkinBlock = (CarvedWhitePumpkinBlock)CARVED_WHITE_PUMPKIN;
                if (level.isEmptyBlock(target) && pumpkinBlock.canSpawnGolem(level, target)) {
                    if (!level.isClientSide()) {
                        level.setBlock(target, pumpkinBlock.defaultBlockState(), 3);
                        level.gameEvent(null, GameEvent.BLOCK_PLACE, target);
                    }
                    dispensed.shrink(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
                }
                return dispensed;
            }
        });
    }

    /**
     * Register all {@link Block Blocks}
     */
    public static void register() {
        registerCompostableBlocks();
        registerDispenseBehaviors();
        OxidizableBlocksRegistryImpl.registerWaxable(Blocks.CREAKING_HEART, WAXED_CREAKING_HEART);
    }

}