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
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 17);
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");

    public final boolean spawnParticles;

    public PlanetBlock(BlockBehaviour.Properties properties, Direction.Axis direction, boolean spawnParticles)
    {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, direction)
                .setValue(DISTANCE, 17).setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
    {
        stateBuilder.add(AXIS, COLUMN, DISTANCE, ROTATION);
    }

    @Override
    public void tick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        serverWorld.setBlock(pos, updateDistance(state, serverWorld, pos), 3);
    }

    @NotNull
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
    {
        int distance = state.getValue(DISTANCE);

        if (distance == Config.ROTATION_DISTANCE.get() + 1)
        {
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE), 4);
        }
        if (Config.ENABLE_ROTATION.get()) {
            if (distance < Config.ROTATION_DISTANCE.get() + 1) {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.TRUE), 4);
            }
            if (distance > Config.ROTATION_DISTANCE.get() + 1) {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE), 4);
            }
        }
        if (!Config.ENABLE_ROTATION.get() && distance < 17)
        {
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 17), 4);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation)
    {
        int distance = state.getValue(DISTANCE);

        if (distance < Config.ROTATION_DISTANCE.get() + 1)
        {
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE), 4);
        }
        if (Config.ENABLE_ROTATION.get()) {
            if (distance == Config.ROTATION_DISTANCE.get() + 1) {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.TRUE), 4);
            }
            if (distance < Config.ROTATION_DISTANCE.get() + 1) {
                world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE), 4);
            }
        }
        if (!Config.ENABLE_ROTATION.get() && distance < 17)
        {
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 17), 4);
        }
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor worldAccessor, BlockPos pos, BlockPos pos2) {
        int distance = state.getValue(DISTANCE);
        int i = getDistance(state2) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            worldAccessor.scheduleTick(pos, this, 1);
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

        if (blockEast == this && axisX && distance < Config.ROTATION_DISTANCE.get() + 1)
        {
            if (blockWest == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Boolean.TRUE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Boolean.TRUE);
        }

        if (blockEast == this && axisX && distance == Config.ROTATION_DISTANCE.get() + 1)
        {
            if (blockWest == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Boolean.FALSE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Boolean.FALSE);
        }
        
        if (blockWest == this && axisX && distance < Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Boolean.TRUE);

        if (blockWest == this && axisX && distance == Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Boolean.FALSE);

        if (blockAbove == this && axisY && distance < Config.ROTATION_DISTANCE.get() + 1)
        {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Boolean.TRUE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Boolean.TRUE);
        }

        if (blockAbove == this && axisY && distance == Config.ROTATION_DISTANCE.get() + 1)
        {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Boolean.FALSE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Boolean.FALSE);
        }
        if (blockBelow == this && axisY && distance < Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Boolean.TRUE);

        if (blockBelow == this && axisY && distance == Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Boolean.FALSE);

        if (blockNorth == this && axisZ && distance < Config.ROTATION_DISTANCE.get() + 1)
        {
            if (blockSouth == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Boolean.TRUE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Boolean.TRUE);
        }

        if (blockNorth == this && axisZ && distance == Config.ROTATION_DISTANCE.get() + 1)
        {
            if (blockSouth == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Boolean.FALSE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Boolean.FALSE);
        }
        if (blockSouth == this && axisZ && distance < Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Boolean.TRUE);

        if (blockSouth == this && axisZ && distance == Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Boolean.FALSE);

        if (distance < Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.NONE).setValue(ROTATION, Boolean.TRUE);
        return state.setValue(COLUMN, ColumnBlockStates.NONE).setValue(ROTATION, Boolean.FALSE);
    }

    private static BlockState updateDistance(BlockState state, LevelAccessor world, BlockPos pos) {
        int i = 17;
        BlockPos.MutableBlockPos posMutable = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            posMutable.setWithOffset(pos, direction);
            i = Math.min(i, getDistance(world.getBlockState(posMutable)) + 1);
            if (i == 1) {
                break;
            }
        }

        return state.setValue(DISTANCE, i);
    }

    private static int getDistance(BlockState state) {

        if (state.is(Blocks.REDSTONE_LAMP) && state.getValue(BlockStateProperties.LIT)) {
            return 0;
        }
        if (state.getBlock() instanceof PlanetBlock) {
            return state.getValue(DISTANCE);
        }
        return Config.ROTATION_DISTANCE.get() + 1;
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
