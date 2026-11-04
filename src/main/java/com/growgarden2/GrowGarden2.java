package com.growgarden2;

import com.growgarden2.areas.AreaEvents;
import com.growgarden2.registry.ModBlocks;
import com.growgarden2.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GrowGarden2.MOD_ID)
public class GrowGarden2 {
    public static final String MOD_ID = "growgarden2";

    public GrowGarden2() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(AreaEvents.class);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept(ModItems.GARDEN_MARKER);
            event.accept(ModItems.SHOP_MARKER);
            event.accept(ModItems.SALES_MARKER);
        }
    }
}
