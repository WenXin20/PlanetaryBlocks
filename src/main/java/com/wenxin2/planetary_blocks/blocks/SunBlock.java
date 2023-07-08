package com.wenxin2.planetary_blocks.blocks;

import com.wenxin2.planetary_blocks.init.Config;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SunBlock extends PlanetBlock
{
    public final boolean spawnParticles;

    public SunBlock(Properties properties, Direction.Axis direction, boolean spawnParticles)
    {
        super(properties, direction, spawnParticles);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(DISTANCE, 7)
                .setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity)
    {
        if (entity instanceof LivingEntity && !entity.fireImmune() && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity) && Config.SUN_BURNING.get())
        {
            entity.hurt(DamageSource.LAVA, 2.0F);
            entity.setSecondsOnFire(25);
        }
        else if (!entity.fireImmune() && !(entity instanceof LivingEntity) && Config.SUN_BURNING.get())
        {
            entity.hurt(DamageSource.LAVA, 2.0F);
            entity.setSecondsOnFire(16);
        }
        super.stepOn(world, pos, state, entity);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource source)
    {
        double posX = (double)pos.getX() + 0.5D;
        double posY = (double)pos.getY() + 1.0D;
        double posZ = (double)pos.getZ() + 0.5D;

        if (world.getBlockState(pos).getValue(DISTANCE) < 7 && Config.SUN_PARTICLES.get())
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

        if (source.nextInt(100) == 0)
        {
            double posXDouble = (double)pos.getX() + source.nextDouble();
            double posZDouble = (double)pos.getZ() + source.nextDouble();
            world.addParticle(ParticleTypes.LAVA, posXDouble, posY, posZDouble, 0.0D, 0.0D, 0.0D);
            world.playLocalSound(posXDouble, posY, posZDouble, SoundEvents.LAVA_POP,
                    SoundSource.BLOCKS, 0.2F + source.nextFloat() * 0.2F, 0.9F + source.nextFloat() * 0.15F, false);
        }

        if (source.nextInt(200) == 0)
        {
            world.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.LAVA_AMBIENT,
                    SoundSource.BLOCKS, 0.2F + source.nextFloat() * 0.2F, 0.9F + source.nextFloat() * 0.15F, false);
        }
    }
}
