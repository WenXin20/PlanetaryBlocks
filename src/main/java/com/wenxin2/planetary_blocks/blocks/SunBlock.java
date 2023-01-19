package com.wenxin2.planetary_blocks.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SunBlock extends PlanetBlock
{
    private final boolean spawnParticles;

    public SunBlock(Properties properties, Direction.Axis direction, boolean spawnParticles)
    {
        super(properties, direction);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, 0)
                .setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity)
    {
        if (entity instanceof LivingEntity && !entity.fireImmune() && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity))
        {
            entity.hurt(DamageSource.LAVA, 2.0F);
            entity.setSecondsOnFire(25);
        } else if (!entity.fireImmune() && !(entity instanceof LivingEntity))
        {
            entity.hurt(DamageSource.LAVA, 2.0F);
            entity.setSecondsOnFire(16);
        }
        super.stepOn(world, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource source)
    {
        double posX = (double)pos.getX() + 0.5D;
        double posY = (double)pos.getY() + 1.0D;
        double posZ = (double)pos.getZ() + 0.5D;

        if (world.getBlockState(pos).getValue(POWERED) > 0)
        {
            if (this.spawnParticles && source.nextInt(2) == 0)
            {
                for (int i = 0; i < source.nextInt(1) + 1; ++i)
                {
                    world.addParticle(ParticleTypes.LAVA, posX, posY, posZ, (double) (source.nextFloat() / 6.0F), 5.0E-5D, (double) (source.nextFloat() / 6.0F));
                    world.addParticle(ParticleTypes.SMOKE, posX, posY, posZ, (double) (source.nextFloat() / 8.0F), 5.0E-5D, (double) (source.nextFloat() / 8.0F));
                    world.addParticle(ParticleTypes.FLAME, posX, posY, posZ, (double) (source.nextFloat() / 8.0F), 5.0E-5D, (double) (source.nextFloat() / 8.0F));
                }
            }
        }
    }
}
