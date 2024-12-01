package org.hendrix.betterpalegarden.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.hendrix.betterpalegarden.block.WhitePumpkinBlock;
import org.hendrix.betterpalegarden.utils.IdentifierUtils;

/**
 * Implementation class for a {@link SnowGolemEntity Snow Golem} wearing a {@link WhitePumpkinBlock White Pumpkin}
 */
public class WhitePumpkinSnowGolemEntity extends SnowGolemEntity {

    /**
     * Constructor. Set the {@link World Entity's World}
     *
     * @param entityType The {@link EntityType Entity Type}
     * @param world The {@link World World reference}
     */
    public WhitePumpkinSnowGolemEntity(final EntityType<? extends SnowGolemEntity> entityType, final World world) {
        super(entityType, world);
    }

    /**
     * Drop the {@link WhitePumpkinBlock White Pumpkin} when the {@link SnowGolemEntity Snow Golem} is sheared
     *
     * @param world The {@link World World reference}
     * @param shearedSoundCategory The {@link SoundCategory Sheared Sound Category}
     * @param shears The {@link ItemStack Shears Item Stack}
     */
    @Override
    public void sheared(final ServerWorld world, final SoundCategory shearedSoundCategory, final ItemStack shears) {
        world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SNOW_GOLEM_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
        this.setHasPumpkin(false);
        this.forEachShearedItem(world, RegistryKey.of(RegistryKeys.LOOT_TABLE, IdentifierUtils.modIdentifier("shearing/snow_golem")), shears, (serverWorld, itemStack) -> this.dropStack(serverWorld, itemStack, this.getStandingEyeHeight()));
    }
}
