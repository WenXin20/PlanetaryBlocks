package com.wenxin2.planetary_blocks.init;

import com.wenxin2.planetary_blocks.PlanetaryBlocks;
import com.wenxin2.planetary_blocks.blocks.ClassicMoonBlock;
import com.wenxin2.planetary_blocks.blocks.ClassicSunBlock;
import com.wenxin2.planetary_blocks.blocks.EarthBlock;
import com.wenxin2.planetary_blocks.blocks.PlanetBlock;
import com.wenxin2.planetary_blocks.blocks.SunBlock;
import com.wenxin2.planetary_blocks.items.RotatorItem;
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

    public static final RegistryObject<Block> CLASSIC_MOON;
    public static final RegistryObject<Block> CLASSIC_SUN;
    public static final RegistryObject<Block> EARTH;
    public static final RegistryObject<Block> MARS;
    public static final RegistryObject<Block> MERCURY;
    public static final RegistryObject<Block> SUN;
    public static final RegistryObject<Block> VENUS;

    public static final RegistryObject<Item> PLANET_ROTATOR;

    static
    {
        CLASSIC_SUN = registerBlock("classic_sun",
                () -> new ClassicSunBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 15)
                        .requiresCorrectToolForDrops().emissiveRendering(ModRegistry::always), Boolean.TRUE), PlanetaryBlocks.CREATIVE_TAB);
        CLASSIC_MOON = registerBlock("classic_moon",
                () -> new ClassicMoonBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SNOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 3).randomTicks()
                        .requiresCorrectToolForDrops(), Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        SUN = registerBlock("sun_block",
                () -> new SunBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW)
                        .sound(SoundType.STONE).strength(3.5F, 100.0F).lightLevel(s -> 15)
                        .requiresCorrectToolForDrops().emissiveRendering(ModRegistry::always), Direction.Axis.Y, Boolean.TRUE), PlanetaryBlocks.CREATIVE_TAB);
        MERCURY = registerBlock("mercury_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y, Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        VENUS = registerBlock("venus_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_YELLOW)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y, Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        EARTH = registerBlock("earth_block",
                () -> new EarthBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F).randomTicks()
                        .requiresCorrectToolForDrops(), Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);
        MARS = registerBlock("mars_block",
                () -> new PlanetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE)
                        .sound(SoundType.STONE).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops(), Direction.Axis.Y, Boolean.FALSE), PlanetaryBlocks.CREATIVE_TAB);

        PLANET_ROTATOR = registerItem("planet_rotator",
                () -> new RotatorItem(new Item.Properties().tab(PlanetaryBlocks.CREATIVE_TAB)));
    }

    public static RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> block, CreativeModeTab tab)
    {
        RegistryObject<Block> blocks = PlanetaryBlocks.BLOCKS.register(name, block);
        PlanetaryBlocks.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab)));
        return blocks;
    }

    public static RegistryObject<Item> registerItem(String name, Supplier<? extends Item> item)
    {
        return PlanetaryBlocks.ITEMS.register(name, item);
    }

    private static boolean always(BlockState state, BlockGetter block, BlockPos pos)
    {
        return true;
    }
}
