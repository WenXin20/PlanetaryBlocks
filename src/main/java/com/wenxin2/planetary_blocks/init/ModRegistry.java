package com.wenxin2.planetary_blocks.init;

import com.wenxin2.planetary_blocks.PlanetaryBlocks;
import com.wenxin2.planetary_blocks.blocks.EarthBlock;
import com.wenxin2.planetary_blocks.blocks.PlanetBlock;
import com.wenxin2.planetary_blocks.blocks.SunBlock;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = PlanetaryBlocks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {

    public static final RegistryObject<Block> CLASSIC_SUN;
    public static final RegistryObject<Block> EARTH;
    public static final RegistryObject<Block> MARS;
    public static final RegistryObject<Block> SUN;

    static
    {
        CLASSIC_SUN = registerBlock("classic_sun",
                () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 15)
                        .requiresCorrectToolForDrops().emissiveRendering(ModRegistry::always)), PlanetaryBlocks.CREATIVE_TAB);
        SUN = registerBlock("sun_block",
                () -> new SunBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 15)
                        .requiresCorrectToolForDrops().emissiveRendering(ModRegistry::always), Direction.Axis.Y, Boolean.TRUE), PlanetaryBlocks.CREATIVE_TAB);
        EARTH = registerBlock("earth_block",
                () -> new EarthBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F).randomTicks()
                        .requiresCorrectToolForDrops(), Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        MARS = registerBlock("mars_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y, Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
    }

    public static RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> block, CreativeModeTab tab)
    {
        RegistryObject<Block> blocks = PlanetaryBlocks.BLOCKS.register(name, block);
        PlanetaryBlocks.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab)));
        return blocks;
    }

    private static boolean always(BlockState state, BlockGetter block, BlockPos pos)
    {
        return true;
    }
}
