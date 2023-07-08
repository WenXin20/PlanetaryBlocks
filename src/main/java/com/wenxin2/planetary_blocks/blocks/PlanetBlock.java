package com.wenxin2.planetary_blocks.blocks;

import com.wenxin2.planetary_blocks.init.Config;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");

    public final boolean spawnParticles;

    public PlanetBlock(BlockBehaviour.Properties properties, Direction.Axis direction, boolean spawnParticles)
    {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction)
                .setValue(DISTANCE, 7).setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
    {
        stateBuilder.add(AXIS, COLUMN, DISTANCE, ROTATION);
    }

//    public boolean isRandomlyTicking(BlockState state) {
//        return state.getValue(DISTANCE) == 7;
//    }

//    @Override
//    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
//        world.setBlock(pos, updateDistance(state, world, pos), 3);
//        world.setBlock(pos, updateRotation(state, world, pos), 3);
//    }
//
//    @NotNull
//    @Override
//    @ParametersAreNonnullByDefault
//    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos)
//    {
//        super.updateShape(state, direction, neighborState, world, pos, neighborPos);
//
//        Block blockAbove = world.getBlockState(pos.above()).getBlock();
//        Block blockBelow = world.getBlockState(pos.below()).getBlock();
//        Block blockNorth = world.getBlockState(pos.north()).getBlock();
//        Block blockSouth = world.getBlockState(pos.south()).getBlock();
//        Block blockEast = world.getBlockState(pos.east()).getBlock();
//        Block blockWest = world.getBlockState(pos.west()).getBlock();
//
//        boolean axisX = state.getValue(AXIS) == Direction.Axis.X;
//        boolean axisY = state.getValue(AXIS) == Direction.Axis.Y;
//        boolean axisZ = state.getValue(AXIS) == Direction.Axis.Z;
//
//        int i = getDistanceAt(neighborState) + 1;
//        if (i != 1 || state.getValue(DISTANCE) != i) {
//            world.scheduleTick(pos, this, 1);
//        }
//
//        if (blockEast == this && axisX)
//        {
//            if (blockWest == this)
//                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
//            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
//        }
//        if (blockWest == this && axisX)
//            return state.setValue(COLUMN, ColumnBlockStates.TOP);
//
//        if (blockAbove == this && axisY)
//        {
//            if (blockBelow == this)
//                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
//            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
//        }
//        if (blockBelow == this && axisY)
//            return state.setValue(COLUMN, ColumnBlockStates.TOP);
//
//        if (blockNorth == this && axisZ)
//        {
//            if (blockSouth == this)
//                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE);
//            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM);
//        }
//        if (blockSouth == this && axisZ)
//            return state.setValue(COLUMN, ColumnBlockStates.TOP);
//
//        return state.setValue(COLUMN, ColumnBlockStates.NONE);
//
//    }
//
//    @NotNull
//    @Override
//    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
//    {
//        updateDistance(state, world, pos);
//    }
//
//    @NotNull
//    @Override
//    @ParametersAreNonnullByDefault
//    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation)
//    {
//        updateDistance(state, world, pos);
//        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
//    }
//
////    public void updateRedstone(BlockState state, Level world, BlockPos pos)
////    {
////        if (!world.isClientSide)
////        {
////            int power = world.getBestNeighborSignal(pos);
////            world.setBlock(pos, state.setValue(DISTANCE, Mth.clamp(power, 0, 15)), 1 | 2 | 4);
////            world.scheduleTick(pos, this, 4);
////            this.updateRotation(state, world, pos);
//////            world.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 5.25F, 0.05F);
////        }
////    }
//
//    private static BlockState updateDistance(BlockState state, LevelAccessor world, BlockPos pos) {
//        int i = 7;
//        BlockPos.MutableBlockPos posMutable = new BlockPos.MutableBlockPos();
//
//        for(Direction direction : Direction.values()) {
//            posMutable.setWithOffset(pos, direction);
//            i = Math.min(i, getDistanceAt(world.getBlockState(posMutable)) + 1);
//            if (i == 1) break;
//        }
//        return state.setValue(DISTANCE, i);
//    }
//
//    private static int getDistanceAt(@NotNull BlockState state) {
//        if (/*state.isSignalSource()*/ state.is(Blocks.REDSTONE_BLOCK)) {
////            if (state.hasProperty(BlockStateProperties.POWERED) && !state.getValue(BlockStateProperties.POWERED))
////                return 7;
////            if (state.hasProperty(BlockStateProperties.POWER) && state.getValue(BlockStateProperties.POWER) == 0)
////                return 7;
//            return 0;
//        }
//        /*else if (state.is(Blocks.REDSTONE_WIRE) && state.getValue(BlockStateProperties.POWER) > 0) {
//            return 0;
//        }*/ if (state.getBlock() instanceof PlanetBlock) {
//            return state.getValue(DISTANCE);
//        }
//        return 7;
//    }
//
//    public BlockState updateRotation(BlockState state, Level world, BlockPos pos)
//    {
//        if (!world.isClientSide)
//        {
//            int bestSignal = world.getBestNeighborSignal(pos);
//            int distance = world.getBlockState(pos).getValue(DISTANCE);
//
//            if (distance < 7)
//            {
//                world.scheduleTick(pos, this, 4);
//                world.setBlock(pos, state.setValue(ROTATION, Config.ENABLE_ROTATION.get())/*.setValue(DISTANCE, Mth.clamp(bestSignal, 0, 15))*/, 4);
//            }
//            else {
//                world.scheduleTick(pos, this, 4);
//                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE)/*.setValue(DISTANCE, 0)*/, 4);
//            }
//            if (!Config.ENABLE_ROTATION.get() && world.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE)
//            {
//                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 7), 4);
//            }
//        }
//        return state;
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context)
//    {
//        boolean rotation = false;
//        if (!Config.ENABLE_ROTATION.get())
//        {
//            rotation = Config.ENABLE_ROTATION.get();
//        }
//        else context.getLevel().hasNeighborSignal(context.getClickedPos());
//
//        BlockState blockstate = this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
//                .setValue(COLUMN, ColumnBlockStates.NONE).setValue(ROTATION, rotation);
//
////        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
////                .setValue(COLUMN, ColumnBlockStates.NONE)
////                .setValue(ROTATION, rotation);
//        return updateDistance(blockstate, context.getLevel(), context.getClickedPos());
//    }

//    @NotNull
//    @Override
//    @ParametersAreNonnullByDefault
//    public int getSignal(BlockState state, BlockGetter block, BlockPos pos, Direction side)
//    {
////        if (block instanceof Level world)
////        {
////            if (world.getBlockState(pos.above()).is(this))
//                return Math.max(0, state.getValue(DISTANCE) - 1);
////            if (world.getBlockState(pos.below()).is(this))
////                return Math.max(0, state.getValue(DISTANCE) - 1);
////            if (world.getBlockState(pos.north()).is(this))
////                return Math.max(0, state.getValue(DISTANCE) - 1);
////            if (world.getBlockState(pos.south()).is(this))
////                return Math.max(0, state.getValue(DISTANCE) - 1);
////            if (world.getBlockState(pos.east()).is(this))
////                return Math.max(0, state.getValue(DISTANCE) - 1);
////            if (world.getBlockState(pos.west()).is(this))
////                return Math.max(0, state.getValue(DISTANCE) - 1);
////        }
////        return 1;
//    }


//    @Override
//    public boolean isRandomlyTicking(BlockState state) {
//        return state.getValue(DISTANCE) == 7;
//    }

//    public void randomTick(BlockState p_221379_, ServerLevel p_221380_, BlockPos p_221381_, RandomSource p_221382_) {
//        if (this.decaying(p_221379_)) {
//            dropResources(p_221379_, p_221380_, p_221381_);
//            p_221380_.removeBlock(p_221381_, false);
//        }
//
//    }

//    protected boolean decaying(BlockState state) {
//        return state.getValue(DISTANCE) == 7;
//    }

    @Override
    public void tick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
//        int distance = serverWorld.getBlockState(pos).getValue(DISTANCE);
//        if (distance < 7) {
//            state.cycle(ROTATION);
//        }
        serverWorld.setBlock(pos, updateDistance(state, serverWorld, pos), 3);
//        serverWorld.setBlock(pos, updateRotation(state, serverWorld, pos), 3);
    }

    @NotNull
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
    {
//        this.updateRotation(state, world, pos);

        int distance = state.getValue(DISTANCE);

        if (distance == 7 /*&& world.getBestNeighborSignal(pos) > 0*/)
        {
//            state.setValue(ROTATION, Boolean.FALSE);
//            world.scheduleTick(pos, this, 4);
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE)/*.setValue(DISTANCE, 0)*/, 4);
        }
        if (distance < 7 && Config.ENABLE_ROTATION.get()) {
//            state.setValue(ROTATION, Boolean.TRUE);
//            world.scheduleTick(pos, this, 4);
            world.setBlock(pos, state.setValue(ROTATION, Boolean.TRUE), 4);
        }
        if (!Config.ENABLE_ROTATION.get() && distance < 7 /*worldAccessor.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE*/)
        {
//            state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 7);
            world.setBlock(pos, state/*.setValue(ROTATION, Boolean.FALSE)*/.setValue(DISTANCE, 7), 4);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation)
    {
//        this.updateRotation(state, world, pos);

        int distance = state.getValue(DISTANCE);

        if (distance == 7 /*&& world.getBestNeighborSignal(pos) > 0*/)
        {
//            state.setValue(ROTATION, Boolean.FALSE);
//            world.scheduleTick(pos, this, 4);
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE)/*.setValue(DISTANCE, 0)*/, 4);
        }
        if (distance < 7 && Config.ENABLE_ROTATION.get()) {
//            state.setValue(ROTATION, Boolean.TRUE);
//            world.scheduleTick(pos, this, 4);
            world.setBlock(pos, state.setValue(ROTATION, Boolean.TRUE), 4);
        }
        if (!Config.ENABLE_ROTATION.get() && distance < 7 /*worldAccessor.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE*/)
        {
//            state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 7);
            world.setBlock(pos, state/*.setValue(ROTATION, Boolean.FALSE)*/.setValue(DISTANCE, 7), 4);
        }
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
    }

//    public int getLightBlock(BlockState p_54460_, BlockGetter p_54461_, BlockPos p_54462_) {
//        return 1;
//    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor worldAccessor, BlockPos pos, BlockPos pos2) {
        int i = getDistanceAt(state2) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            worldAccessor.scheduleTick(pos, this, 1);
//            this.updateRotation(state, worldAccessor, pos);
        }

        Block blockAbove = worldAccessor.getBlockState(pos.above()).getBlock();
        Block blockBelow = worldAccessor.getBlockState(pos.below()).getBlock();
        Block blockNorth = worldAccessor.getBlockState(pos.north()).getBlock();
        Block blockSouth = worldAccessor.getBlockState(pos.south()).getBlock();
        Block blockEast = worldAccessor.getBlockState(pos.east()).getBlock();
        Block blockWest = worldAccessor.getBlockState(pos.west()).getBlock();

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

    private static BlockState updateDistance(BlockState state, LevelAccessor world, BlockPos pos) {
        int i = 7;
        BlockPos.MutableBlockPos posMutable = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            posMutable.setWithOffset(pos, direction);
//            if (world.getDirectSignal(posMutable.setWithOffset(pos, direction), direction) > 0) {
                i = Math.min(i, getDistanceAt(world.getBlockState(posMutable)) + 1);
                if (i == 1) {
                    break;
                }
//            }
        }

        return state.setValue(DISTANCE, i);
    }

    private static int getDistanceAt(BlockState state) {

        if (state.is(Blocks.REDSTONE_LAMP) && state.getValue(BlockStateProperties.LIT)) {
            return 0;
        }
        if (state.getBlock() instanceof PlanetBlock) {
//            state.cycle(ROTATION);
            return state.getValue(DISTANCE);
        }
        return 7;
    }

    public BlockState updateRotation(BlockState state, LevelAccessor world, BlockPos pos)
    {
//        if (!world.isClientSide)
//        {
//            int bestSignal = world.getBestNeighborSignal(pos);
            int distance = world.getBlockState(pos).getValue(DISTANCE);

            if (distance < 7 && Config.ENABLE_ROTATION.get() /*&& world.getBestNeighborSignal(pos) > 0*/)
            {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Config.ENABLE_ROTATION.get()).setValue(DISTANCE, distance), 4);
            }
            else {
                world.scheduleTick(pos, this, 4);
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE)/*.setValue(DISTANCE, 0)*/, 4);
            }
            if (!Config.ENABLE_ROTATION.get() && world.getBlockState(pos).getValue(ROTATION) == Boolean.TRUE)
            {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 7), 4);
            }
//        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos posClicked = context.getClickedPos();
        Level world = context.getLevel();
        BlockState blockstate = this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis())
                .setValue(COLUMN, ColumnBlockStates.NONE).setValue(ROTATION, Boolean.FALSE);

        return updateDistance(blockstate, world, posClicked);
    }
}
