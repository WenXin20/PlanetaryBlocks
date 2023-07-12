package com.wenxin2.planetary_blocks.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PedestalBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock
{
    public static final EnumProperty<ColumnBlockStates> COLUMN = EnumProperty.create("column", ColumnBlockStates.class);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape VOXELS_MAIN = Shapes.or(
            Block.box(0, 8, 0, 16, 16, 16),
            Block.box(2, 6, 2, 14, 8, 14),
            Block.box(0, 0, 0, 16, 6, 16)).optimize();
    public static final VoxelShape VOXELS_MAIN_X = Shapes.or(
            Block.box(8, 0, 0, 16, 16, 16),
            Block.box(6, 2, 2, 8, 14, 14),
            Block.box(0, 0, 0, 6, 16, 16)).optimize();
    public static final VoxelShape VOXELS_MAIN_Z = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 8),
            Block.box(2, 2, 8, 14, 14, 10),
            Block.box(0, 0, 10, 16, 16, 16)).optimize();
    public static final VoxelShape VOXELS_TOP = Shapes.or(
            Block.box(0, 8, 0, 16, 16, 16),
            Block.box(2, 0, 2, 14, 8, 14)).optimize();
    public static final VoxelShape VOXELS_TOP_X = Shapes.or(
            Block.box(0, 2, 2, 8, 14, 14),
                    Block.box(8, 0, 0, 16, 16, 16)).optimize();
    public static final VoxelShape VOXELS_TOP_Z = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 8),
            Block.box(2, 2, 8, 14, 14, 16)).optimize();
    public static final VoxelShape VOXELS_MIDDLE =
            Block.box(2, 0, 2, 14, 16, 14);
    public static final VoxelShape VOXELS_MIDDLE_X =
            Block.box(0, 2, 2, 16, 14, 14);
    public static final VoxelShape VOXELS_MIDDLE_Z =
            Block.box(2, 2, 0, 14, 14, 16);
    public static final VoxelShape VOXELS_BOTTOM = Shapes.or(
            Block.box(2, 8, 2, 14, 16, 14),
            Block.box(0, 0, 0, 16, 8, 16)).optimize();
    public static final VoxelShape VOXELS_BOTTOM_X = Shapes.or(
            Block.box(0, 0, 0, 8, 16, 16),
            Block.box(8, 2, 2, 16, 14, 14)).optimize();
    public static final VoxelShape VOXELS_BOTTOM_Z = Shapes.or(
            Block.box(2, 2, 0, 14, 14, 8),
            Block.box(0, 0, 8, 16, 16, 16)).optimize();

    public PedestalBlock(Properties properties, Direction.Axis direction) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction).setValue(COLUMN, ColumnBlockStates.NONE)
                .setValue(POWERED, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AXIS, COLUMN, POWERED, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        final FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
                .setValue(COLUMN, ColumnBlockStates.NONE).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()))
                .setValue(WATERLOGGED, fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8);
    }

    // Precise selection box
    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos,
                               final CollisionContext context)
    {
        switch (state.getValue(COLUMN))
        {
            case TOP:
                return switch (state.getValue(AXIS)) {
                    case X -> VOXELS_TOP_X;
                    case Z -> VOXELS_TOP_Z;
                    default -> VOXELS_TOP;
                };
            case MIDDLE:
                return switch (state.getValue(AXIS)) {
                    case X -> VOXELS_MIDDLE_X;
                    case Z -> VOXELS_MIDDLE_Z;
                    default -> VOXELS_MIDDLE;
                };
            case BOTTOM:
                return switch (state.getValue(AXIS)) {
                    case X -> VOXELS_BOTTOM_X;
                    case Z -> VOXELS_BOTTOM_Z;
                    default -> VOXELS_BOTTOM;
                };
            case NONE:
                return switch (state.getValue(AXIS)) {
                    case X -> VOXELS_MAIN_X;
                    case Z -> VOXELS_MAIN_Z;
                    default -> VOXELS_MAIN;
                };
            default:
                return VOXELS_MAIN;
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        if (state.getValue(COLUMN) == ColumnBlockStates.TOP || state.getValue(COLUMN) == ColumnBlockStates.BOTTOM || state.getValue(COLUMN) == ColumnBlockStates.NONE) {
            return Shapes.block();
        }
        return this.getCollisionShape(state, blockGetter, pos, CollisionContext.empty());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor worldAccessor, BlockPos pos, BlockPos facingPos) {
        Block blockAbove = worldAccessor.getBlockState(pos.above()).getBlock();
        Block blockBelow = worldAccessor.getBlockState(pos.below()).getBlock();
        Block blockNorth = worldAccessor.getBlockState(pos.north()).getBlock();
        Block blockSouth = worldAccessor.getBlockState(pos.south()).getBlock();
        Block blockEast = worldAccessor.getBlockState(pos.east()).getBlock();
        Block blockWest = worldAccessor.getBlockState(pos.west()).getBlock();

        BlockState stateAbove = worldAccessor.getBlockState(pos.above());
        BlockState stateBelow = worldAccessor.getBlockState(pos.below());
        BlockState stateNorth = worldAccessor.getBlockState(pos.north());
        BlockState stateSouth = worldAccessor.getBlockState(pos.south());
        BlockState stateEast = worldAccessor.getBlockState(pos.east());
        BlockState stateWest = worldAccessor.getBlockState(pos.west());

        boolean isAxisX = state.getValue(AXIS) == Direction.Axis.X;
        boolean isAxisY = state.getValue(AXIS) == Direction.Axis.Y;
        boolean isAxisZ = state.getValue(AXIS) == Direction.Axis.Z;

        if (blockEast instanceof PedestalBlock && isAxisX && stateEast.getValue(AXIS) == Direction.Axis.X) {
            if (blockWest instanceof PedestalBlock && stateWest.getValue(AXIS) == Direction.Axis.X)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockWest instanceof PedestalBlock && isAxisX && stateWest.getValue(AXIS) == Direction.Axis.X)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockAbove instanceof PedestalBlock && isAxisY && stateAbove.getValue(AXIS) == Direction.Axis.Y) {
            if (blockBelow instanceof PedestalBlock && stateBelow.getValue(AXIS) == Direction.Axis.Y)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockBelow instanceof PedestalBlock && isAxisY && stateBelow.getValue(AXIS) == Direction.Axis.Y)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (blockNorth instanceof PedestalBlock && isAxisZ && stateNorth.getValue(AXIS) == Direction.Axis.Z) {
            if (blockSouth instanceof PedestalBlock && stateSouth.getValue(AXIS) == Direction.Axis.Z)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
        }
        if (blockSouth instanceof PedestalBlock && isAxisZ && stateSouth.getValue(AXIS) == Direction.Axis.Z)
            return state.setValue(COLUMN, ColumnBlockStates.TOP);

        if (state.getValue(WATERLOGGED))
            worldAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(worldAccessor));

        return state.setValue(COLUMN, ColumnBlockStates.NONE);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return false;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean b) {
        if (!world.isClientSide) {
            boolean flag = state.getValue(POWERED);
            if (flag != world.hasNeighborSignal(pos)) {
                if (flag) {
                    world.scheduleTick(pos, this, 4);
                } else if (world.hasNeighborSignal(pos)) {
                    world.setBlock(pos, state.cycle(POWERED), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        if (state.getValue(POWERED) && !serverWorld.hasNeighborSignal(pos)) {
            serverWorld.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 2);
        } else if (serverWorld.hasNeighborSignal(pos)) {
            serverWorld.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), 2);
            System.out.print(state.getValue(POWERED) + " ");
        }
    }

    @Override
    public FluidState getFluidState(final BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
