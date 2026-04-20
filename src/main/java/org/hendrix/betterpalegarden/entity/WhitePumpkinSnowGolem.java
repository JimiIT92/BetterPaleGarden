package org.hendrix.betterpalegarden.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.hendrix.betterpalegarden.core.BPGLootTables;
import org.jspecify.annotations.NonNull;

/**
 * Implementation class for a Snow Golem wearing a White Pumpkin
 */
public class WhitePumpkinSnowGolem extends SnowGolem {

    /**
     * Constructor. Set the entity properties
     *
     * @param type The entity type
     * @param level The {@link Level} reference
     */
    public WhitePumpkinSnowGolem(final EntityType<? extends SnowGolem> type, final Level level) {
        super(type, level);
    }

    /**
     * Drop the white pumpkin when the golem is sheared
     *
     * @param level The {@link ServerLevel} reference
     * @param soundSource The {@link SoundSource}
     * @param tool The {@link ItemStack} used to shear
     */
    @Override
    public void shear(final ServerLevel level, final @NonNull SoundSource soundSource, final @NonNull ItemStack tool) {
        level.playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
        this.setPumpkin(false);
        this.dropFromShearingLootTable(level, BPGLootTables.SNOW_GOLEM, tool, (serverLevel, drop) -> this.spawnAtLocation(serverLevel, drop, this.getEyeHeight()));
    }

}
