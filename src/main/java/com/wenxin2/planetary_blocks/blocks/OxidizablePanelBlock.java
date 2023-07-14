package com.wenxin2.planetary_blocks.blocks;

import com.wenxin2.planetary_blocks.utils.OxidationMappings;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class OxidizablePanelBlock extends IronBarsBlock implements SimpleWaterloggedBlock, WeatheringCopper
{
    private final WeatherState weatherState;

    public OxidizablePanelBlock(Properties properties, WeatherState weatherState) {
        super(properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        this.onRandomTick(state, serverWorld, pos, source);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return getNext(state).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.ofNullable(OxidationMappings.NEXT_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }
}
