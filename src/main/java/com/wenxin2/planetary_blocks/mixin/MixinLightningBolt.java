package com.wenxin2.planetary_blocks.mixin;

import com.wenxin2.planetary_blocks.blocks.OxidizablePanelBlock;
import com.wenxin2.planetary_blocks.blocks.OxidizablePedestalBlock;
import com.wenxin2.planetary_blocks.utils.OxidationMappings;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightningBolt.class)
public class MixinLightningBolt {

    @Inject(at = @At("HEAD"), method = "randomStepCleaningCopper", cancellable = true)
    private static void randomStepCleaningCopper(Level world, BlockPos pos, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        for(BlockPos posRandomCube : BlockPos.randomInCube(world.random, 10, pos, 1)) {
            BlockState state = world.getBlockState(posRandomCube);
            if (state.getBlock() instanceof WeatheringCopper || state.getBlock() instanceof OxidizablePedestalBlock || state.getBlock() instanceof OxidizablePanelBlock) {
                OxidationMappings.getPrevious(state).ifPresent((statePrevious) -> {
                    world.setBlockAndUpdate(posRandomCube, statePrevious);
                    System.out.print("Pedestal Strike ");
                });
                WeatheringCopper.getPrevious(state).ifPresent((statePrevious) -> {
                    world.setBlockAndUpdate(posRandomCube, statePrevious);
                    System.out.print("Normal Strike ");
                });
                world.levelEvent(3002, posRandomCube, -1);
                cir.setReturnValue(Optional.empty());
            }
            System.out.print("Strike ");
        }
        cir.setReturnValue(Optional.empty());
    }


    @Inject(at = @At("HEAD"), method = "clearCopperOnLightningStrike")
    private static void clearCopperOnLightningStrike(Level world, BlockPos pos, CallbackInfo ci) {
        BlockState state = world.getBlockState(pos);
        BlockPos posRelative;
        BlockState stateRelative;
        if (state.is(Blocks.LIGHTNING_ROD)) {
            posRelative = pos.relative(state.getValue(LightningRodBlock.FACING).getOpposite());
            stateRelative = world.getBlockState(posRelative);
            System.out.print("Clear Lightning ");
        } else {
            posRelative = pos;
            stateRelative = state;
        }

        if (stateRelative.getBlock() instanceof WeatheringCopper || stateRelative.getBlock() instanceof OxidizablePedestalBlock || state.getBlock() instanceof OxidizablePanelBlock) {
            world.setBlockAndUpdate(posRelative, WeatheringCopper.getFirst(world.getBlockState(posRelative)));
            world.setBlockAndUpdate(posRelative, OxidationMappings.getFirst(world.getBlockState(posRelative)));
            BlockPos.MutableBlockPos posMutable = pos.mutable();
            int i = world.random.nextInt(3) + 3;

            for(int j = 0; j < i; ++j) {
                int k = world.random.nextInt(8) + 1;
                LightningBolt.randomWalkCleaningCopper(world, posRelative, posMutable, k);
            }

        }
    }
}
