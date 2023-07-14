package com.wenxin2.planetary_blocks.events;

import com.wenxin2.planetary_blocks.blocks.OxidizablePedestalBlock;
import com.wenxin2.planetary_blocks.utils.OxidationMappings;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RightClickEvents {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();
        BlockState state = world.getBlockState(pos);

        if (player.getItemInHand(hand).getItem() instanceof HoneycombItem) {
            Optional<BlockState> waxable = OxidationMappings.getWaxables(state);
            if (waxable.isPresent()) {
                ItemStack itemstack = player.getItemInHand(hand);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, itemstack);
                }

                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                player.swing(hand);
                world.setBlock(pos, waxable.get(), 11);
                world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, waxable.get()));
                world.levelEvent(player, LevelEvent.PARTICLES_AND_SOUND_WAX_ON, pos, 0);
            }
        }

        if (player.getItemInHand(hand).getItem() instanceof AxeItem) {
            Optional<BlockState> finalOxidation = Optional.empty();
            Optional<BlockState> previousOxidation = OxidationMappings.getPreviousOxidationState(state);
            if (OxidationMappings.getPreviousOxidationState(state).isPresent()) {
                world.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.levelEvent(player, LevelEvent.PARTICLES_SCRAPE, pos, 0);
                finalOxidation = previousOxidation;
            }
            Optional<BlockState> previousWaxed = OxidationMappings.getWaxOffState(state);
            if (previousWaxed.isPresent()) {
                world.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.levelEvent(player, LevelEvent.PARTICLES_WAX_OFF, pos, 0);
                finalOxidation = previousWaxed;
            }
            if (finalOxidation.isPresent()) {
                world.setBlock(pos, finalOxidation.get(), 11);
                player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                player.swing(hand);
            }
        }
    }
}
