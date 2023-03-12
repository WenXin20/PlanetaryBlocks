package com.wenxin2.planetary_blocks.blocks;

import com.wenxin2.planetary_blocks.init.Config;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class PlanetBlock extends RotatedPillarBlock
{
    public static final EnumProperty<ColumnBlockStates> COLUMN = EnumProperty.create("column", ColumnBlockStates.class);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");

    public final boolean spawnParticles;

    public PlanetBlock(BlockBehaviour.Properties properties, Direction.Axis direction, boolean spawnParticles)
    {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction)
                .setValue(POWERED, Boolean.FALSE).setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
    {
        stateBuilder.add(AXIS, COLUMN, POWERED, ROTATION);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos)
    {
        super.updateShape(state, direction, neighborState, world, pos, neighborPos);

        Block blockAbove = world.getBlockState(pos.above()).getBlock();
        Block blockBelow = world.getBlockState(pos.below()).getBlock();
        Block blockNorth = world.getBlockState(pos.north()).getBlock();
        Block blockSouth = world.getBlockState(pos.south()).getBlock();
        Block blockEast = world.getBlockState(pos.east()).getBlock();
        Block blockWest = world.getBlockState(pos.west()).getBlock();

        boolean axisX = state.getValue(AXIS) == Direction.Axis.X;
        boolean axisY = state.getValue(AXIS) == Direction.Axis.Y;
        boolean axisZ = state.getValue(AXIS) == Direction.Axis.Z;

        if (blockEast == this && axisX)
        {
            if (blockWest == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockWest == this && axisX)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockAbove == this && axisY)
        {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockBelow == this && axisY)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockNorth == this && axisZ)
        {
            if (blockSouth == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockSouth == this && axisZ)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        return state.setValue(COLUMN, ColumnBlockStates.NONE);

    }

    @NotNull
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
    {
        this.updateRedstone(state, world, pos);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation)
    {
        if (world.getBlockState(pos).getBlock() == PlanetBlock.this) {
            this.updateRedstone(state, world, pos);
        }
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
    }

    public void updateRedstone(BlockState state, Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            int power = world.getBestNeighborSignal(pos);
            world.setBlock(pos, state.setValue(POWERED, world.hasNeighborSignal(pos)), 1 | 2 | 4);
            world.scheduleTick(pos, this, 4);
            this.updateRotation(state, world, pos);
//            world.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 5.25F, 0.05F);
        }
    }

    public void updateRotation(BlockState state, Level world, BlockPos pos) {
        BlockState stateAbove = world.getBlockState(pos.above());
        BlockState stateBelow = world.getBlockState(pos.below());
        BlockState stateNorth = world.getBlockState(pos.north());
        BlockState stateSouth = world.getBlockState(pos.south());
        BlockState stateEast = world.getBlockState(pos.east());
        BlockState stateWest = world.getBlockState(pos.west());

        Block blockAbove = world.getBlockState(pos.above()).getBlock();
        Block blockBelow = world.getBlockState(pos.below()).getBlock();
        Block blockNorth = world.getBlockState(pos.north()).getBlock();
        Block blockSouth = world.getBlockState(pos.south()).getBlock();
        Block blockEast = world.getBlockState(pos.east()).getBlock();
        Block blockWest = world.getBlockState(pos.west()).getBlock();

        if (blockAbove == this && stateAbove.getValue(POWERED))
        {
            if (!world.isClientSide) {
                boolean power = world.getBlockState(pos.above()).getValue(POWERED);

                if (power) {
                    world.scheduleTick(pos.below(), this, 8);
                    world.setBlock(pos, state.setValue(ROTATION, Config.enable_rotation.get()).setValue(POWERED, power), 8);
                } else {
                    world.scheduleTick(pos.below(), this, 8);
                    world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, Boolean.FALSE), 8);
                }
                if (!Config.enable_rotation.get() && world.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE) {
                    world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, Boolean.FALSE), 8);
                }
            }
        }

        if (blockBelow == this && stateBelow.getValue(POWERED))
        {
            if (!world.isClientSide) {
                boolean power = world.getBlockState(pos.below()).getValue(POWERED);

                if (power) {
                    world.scheduleTick(pos.above(), this, 8);
                    world.setBlock(pos, state.setValue(ROTATION, Config.enable_rotation.get()).setValue(POWERED, power), 8);
                } else {
                    world.scheduleTick(pos.above(), this, 8);
                    world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, Boolean.FALSE), 8);
                }
                if (!Config.enable_rotation.get() && world.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE) {
                    world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, Boolean.FALSE), 8);
                }
            }
        }

        if (!world.isClientSide) {
            int bestSignal = world.getBestNeighborSignal(pos);
            boolean power = world.getBlockState(pos).getValue(POWERED);

            if (power) {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Config.enable_rotation.get()).setValue(POWERED, Boolean.TRUE), 4);
            } else {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, Boolean.FALSE), 4);
            }
            if (!Config.enable_rotation.get() && world.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE) {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(POWERED, Boolean.FALSE), 4);
            }
        }
    }

    public boolean canPower(BlockState state)
    {
        return this.defaultBlockState().getBlock().equals(this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        boolean rotation = false;
        if (!Config.enable_rotation.get())
        {
            rotation = Config.enable_rotation.get();
        }
        else context.getLevel().hasNeighborSignal(context.getClickedPos());

//        BlockState state = world.getBlockState(clickedPos);
//        BlockState stateNorth = world.getBlockState(clickedPos.north());
//        BlockState stateSouth = world.getBlockState(clickedPos.south());
//        BlockState stateEast = world.getBlockState(clickedPos.east());
//        BlockState stateWest = world.getBlockState(clickedPos.west());
//        BlockState stateAbove = world.getBlockState(clickedPos.above());
//        BlockState stateBelow = world.getBlockState(clickedPos.below());
//
//        Boolean power = this.canPower(state);
//        Boolean powerNorth = this.canPower(stateNorth);
//        Boolean powerSouth = this.canPower(stateSouth);
//        Boolean powerEast = this.canPower(stateEast);
//        Boolean powerWest = this.canPower(stateWest);
//        Boolean powerAbove = this.canPower(stateAbove);
//        Boolean powerBelow = this.canPower(stateBelow);
//
//        Boolean powered = (state.getValue(POWERED) && power)/* || (stateNorth.getValue(POWERED) && powerNorth) || (stateSouth.getValue(POWERED) && powerSouth) || (stateEast.getValue(POWERED) && powerEast) ||
//                (stateWest.getValue(POWERED) && powerWest) || (stateAbove.getValue(POWERED) && powerAbove) || (stateBelow.getValue(POWERED) && powerBelow)*/;

        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
                .setValue(COLUMN, ColumnBlockStates.NONE)
                .setValue(ROTATION, rotation)
                /*.setValue(POWERED, powered)*/;

//        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
//                .setValue(COLUMN, ColumnBlockStates.NONE)
//                .setValue(ROTATION, rotation);
    }

//    @NotNull
//    @Override
//    @ParametersAreNonnullByDefault
//    public int getSignal(BlockState state, BlockGetter block, BlockPos pos, Direction side)
//    {
//        return Math.max(0, state.getValue(POWERED) - 1);
//    }
}
