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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class EarthBlock extends HorizontalDirectionalBlock
{
    public static final EnumProperty<ColumnBlockStates> COLUMN = EnumProperty.create("column", ColumnBlockStates.class);
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 17);
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");
    public static final BooleanProperty NIGHT = BooleanProperty.create("night");

    public final boolean spawnParticles;

    public EarthBlock(Properties properties, boolean spawnParticles) {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.registerDefaultState(this.getStateDefinition().any().setValue(NIGHT, Boolean.FALSE)
                .setValue(DISTANCE, 17).setValue(ROTATION, Boolean.FALSE).setValue(COLUMN, ColumnBlockStates.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(COLUMN, FACING, NIGHT, DISTANCE, ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos posClicked = context.getClickedPos();
        Level world = context.getLevel();
        boolean night = false;

        if (Config.FOREVER_EARTH_NIGHT.get() || Config.EARTH_NIGHT.get())
        {
            night = Config.FOREVER_EARTH_NIGHT.get();
        }
        else if (Config.EARTH_NIGHT.get())
            context.getLevel().isNight();
        else context.getLevel().hasNeighborSignal(context.getClickedPos());

        BlockState blockstate = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(COLUMN, ColumnBlockStates.NONE).setValue(NIGHT, night).setValue(ROTATION, Boolean.FALSE);

        return updateDistance(blockstate, world, posClicked);
    }

    @Override
    public void tick(BlockState state, ServerLevel serverWorld, BlockPos pos, RandomSource source) {
        serverWorld.setBlock(pos, updateDistance(state, serverWorld, pos), 3);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        int distance = state.getValue(DISTANCE);

        if (distance == Config.ROTATION_DISTANCE.get() + 1) {
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
        if (!Config.ENABLE_ROTATION.get() && distance < 17) {
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 17), 4);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos pos2, boolean rotation) {
        int distance = state.getValue(DISTANCE);

        if (distance < Config.ROTATION_DISTANCE.get() + 1) {
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
        if (!Config.ENABLE_ROTATION.get() && distance < 17) {
            world.setBlock(pos, state.setValue(ROTATION, Boolean.FALSE).setValue(DISTANCE, 17), 4);
        }
        super.neighborChanged(state, world, pos, neighborBlock, pos, rotation);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor worldAccessor, BlockPos pos, BlockPos neighborPos) {
        int distance = state.getValue(DISTANCE);
        int i = getDistance(neighborState) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            worldAccessor.scheduleTick(pos, this, 1);
        }
        super.updateShape(state, direction, neighborState, worldAccessor, pos, neighborPos);

        Block blockAbove = worldAccessor.getBlockState(pos.above()).getBlock();
        Block blockBelow = worldAccessor.getBlockState(pos.below()).getBlock();

        if (blockAbove == this && distance < Config.ROTATION_DISTANCE.get() + 1) {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Config.ENABLE_ROTATION.get());
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Config.ENABLE_ROTATION.get());
        }
        if (blockAbove == this && distance == Config.ROTATION_DISTANCE.get() + 1) {
            if (blockBelow == this)
                return state.setValue(COLUMN, ColumnBlockStates.MIDDLE).setValue(ROTATION, Boolean.FALSE);
            return state.setValue(COLUMN, ColumnBlockStates.BOTTOM).setValue(ROTATION, Boolean.FALSE);
        }

        if (blockBelow == this && distance < Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Config.ENABLE_ROTATION.get());
        if (blockBelow == this && distance == Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.TOP).setValue(ROTATION, Boolean.FALSE);

        if (distance < Config.ROTATION_DISTANCE.get() + 1)
            return state.setValue(COLUMN, ColumnBlockStates.NONE).setValue(ROTATION, Config.ENABLE_ROTATION.get());
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
        if (state.getBlock() instanceof PedestalBlock && !state.getValue(BlockStateProperties.POWERED)) {
            return 0;
        }
        if (state.getBlock() instanceof EarthBlock) {
            return state.getValue(DISTANCE);
        }
        return Config.ROTATION_DISTANCE.get() + 1;
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource source)
    {
        if (Config.EARTH_NIGHT.get() && !Config.FOREVER_EARTH_NIGHT.get())
            world.setBlock(pos, state.setValue(NIGHT, world.isNight()), 3);
        if (!Config.EARTH_NIGHT.get() && !Config.FOREVER_EARTH_NIGHT.get() && world.getBlockState(pos).getValue(NIGHT) == Boolean.TRUE)
        {
            world.setBlock(pos, state.setValue(NIGHT, Boolean.FALSE), 3);
        }
        if (Config.FOREVER_EARTH_NIGHT.get() && world.getBlockState(pos).getValue(NIGHT) == Boolean.FALSE)
        {
            world.setBlock(pos, state.setValue(NIGHT, Boolean.TRUE), 3);
        }
    }
}
