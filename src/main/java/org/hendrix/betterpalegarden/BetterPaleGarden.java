package org.hendrix.betterpalegarden;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.hendrix.betterpalegarden.config.BPGConfig;
import org.hendrix.betterpalegarden.core.*;

/**
 * Hendrix's Better Pale Garden
 * Boost the Pale Garden with White Pumpkins, new Resin Blocks and a new Structure!
 */
public final class BetterPaleGarden implements ModInitializer {

    /**
     * The {@link String Mod ID}
     */
    public static final String MOD_ID = "betterpalegarden";

    /**
     * The {@link BPGConfig Mod Configuration}
     */
    private static BPGConfig CONFIG;

    /**
     * The {@link Long maximum Fog Thickness}
     */
    public static final long MAX_FOG_THICKNESS = 128L;

    /**
     * The {@link Long default Fog Thickness}
     */
    public static final long DEFAULT_FOG_THICKNESS = 96L;

    /**
     * Initialize the mod
     */
    @Override
    public void onInitialize() {
        BPGItemGroups.register();
        BPGItems.register();
        BPGBlocks.register();
        BPGEntities.register();
        BPGPaintingVariants.register();
        BPGPlacedFeatures.addToBiomes();

        UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
            final ItemStack itemStack = playerEntity.getStackInHand(hand);
            final BlockPos pos = blockHitResult.getBlockPos();
            final BlockState clickedBlockState = world.getBlockState(pos);
            if(itemStack.isOf(Items.HONEYCOMB) && clickedBlockState.isOf(Blocks.CREAKING_HEART)) {
                if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayerEntity, pos, itemStack);
                }
                if(!playerEntity.isInCreativeMode()) {
                    itemStack.decrement(1);
                }
                final BlockState state = BPGBlocks.WAXED_CREAKING_HEART.getDefaultState().with(CreakingHeartBlock.AXIS, clickedBlockState.get(CreakingHeartBlock.AXIS)).with(CreakingHeartBlock.ACTIVE, clickedBlockState.get(CreakingHeartBlock.ACTIVE)).with(CreakingHeartBlock.NATURAL, clickedBlockState.get(CreakingHeartBlock.NATURAL));
                world.setBlockState(pos, state, Block.NOTIFY_ALL_AND_REDRAW);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(playerEntity, state));
                world.syncWorldEvent(playerEntity, WorldEvents.BLOCK_WAXED, pos, 0);
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

        if(isClothConfigInstalled()) {
            AutoConfig.register(BPGConfig.class, GsonConfigSerializer::new);
        }
    }

    /**
     * Get the {@link #CONFIG Mod Configuration}
     *
     * @return The {@link #CONFIG Mod Configuration}
     */
    public static BPGConfig config() {
        if(CONFIG == null) {
            CONFIG = AutoConfig.getConfigHolder(BPGConfig.class).getConfig();
        }
        return CONFIG;
    }

    /**
     * Check if ClothConfig is installed
     *
     * @return {@link Boolean True if ClothConfig is installed}
     */
    public static boolean isClothConfigInstalled() {
        return FabricLoader.getInstance().isModLoaded("cloth-config");
    }

}