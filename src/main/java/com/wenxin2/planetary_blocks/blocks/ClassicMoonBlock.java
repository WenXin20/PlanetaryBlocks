package com.wenxin2.planetary_blocks.blocks;

import com.wenxin2.planetary_blocks.init.Config;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class ClassicMoonBlock extends Block
{
    public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, 7);

    public final boolean spawnParticles;

    public ClassicMoonBlock(Properties properties, boolean spawnParticles)
    {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(PHASE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
    {
        stateBuilder.add(PHASE);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource source)
    {
        if (Config.classic_moon_phase.get() == -1)
            world.setBlock(pos, state.setValue(PHASE, world.getMoonPhase()), 3);
        if (Config.classic_moon_phase.get() >= 0)
            world.setBlock(pos, state.setValue(PHASE, Config.classic_moon_phase.get()), 3);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        int phase = 1;
        if (Config.classic_moon_phase.get() == -1)
            phase = context.getLevel().getMoonPhase();
        else if (Config.classic_moon_phase.get() >= 0)
            phase = Config.classic_moon_phase.get();

        return this.defaultBlockState().setValue(PHASE, phase);
    }
}
