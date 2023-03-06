package com.wenxin2.planetary_blocks;

import com.mojang.logging.LogUtils;
import com.wenxin2.planetary_blocks.init.Config;
import com.wenxin2.planetary_blocks.init.ModRegistry;
import com.wenxin2.planetary_blocks.paintings.PaintingsInit;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PlanetaryBlocks.MODID)
public class PlanetaryBlocks
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "planetary_blocks";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold blocks/items which will all be registered under the "planetary_blocks" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<PaintingVariant> PAINTINGS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, MODID);

    public static CreativeModeTab CREATIVE_TAB = new CreativeModeTab("planetary_blocks")
    {
        @Override
        public ItemStack makeIcon()
        {
            return ModRegistry.MARS.get().asItem().getDefaultInstance();
        }
    };

    public PlanetaryBlocks()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the Deferred Register to the mod event bus so blocks/items get registered
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        PAINTINGS.register(modEventBus);

        // Register paintings
        PaintingsInit.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
