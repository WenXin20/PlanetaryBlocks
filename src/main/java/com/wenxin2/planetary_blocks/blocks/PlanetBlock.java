package com.wenxin2.planetary_blocks.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class PlanetBlock extends RotatedPillarBlock
{
    public static final IntegerProperty POWERED = BlockStateProperties.POWER;
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");

    public PlanetBlock(BlockBehaviour.Properties properties, Direction.Axis direction)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction)
                .setValue(POWERED, 0).setValue(ROTATION, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AXIS, POWERED, ROTATION);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
    {
        this.updateRedstone(state, world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation)
    {
        this.updateRedstone(state, world, pos);
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
    }

    public void updateRedstone(BlockState state, Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            int power = world.getBestNeighborSignal(pos);
            world.setBlock(pos, state.setValue(POWERED, Mth.clamp(power, 0, 15)), 1 | 2 | 4);
            this.resetPower(state, world, pos);
        }
    }

    public void resetPower(BlockState state, Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            int power = world.getBestNeighborSignal(pos);
            int flag = world.getBlockState(pos).getValue(POWERED);

            if (flag > 0)
            {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Boolean.TRUE).setValue(POWERED, Mth.clamp(power, 0, 15)), 1 | 2 | 4);
                world.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 0.25F, 8.5F);

            }
            else {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, 0), 3);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pos) {
        return this.defaultBlockState().setValue(AXIS, pos.getClickedFace().getAxis())
                .setValue(ROTATION, pos.getLevel().hasNeighborSignal(pos.getClickedPos()));
    }

    public int getSignal(BlockState state, @NotNull BlockGetter block, BlockPos pos, Direction side) {
        return Math.max(0, state.getValue(POWERED) - 1);
    }
}
