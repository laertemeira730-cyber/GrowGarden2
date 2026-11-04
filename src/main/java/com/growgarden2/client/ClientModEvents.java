package com.growgarden2.client;

import com.growgarden2.GrowGarden2;
import com.growgarden2.registry.ModBlocks;
import com.growgarden2.registry.ModMenus;
import com.growgarden2.shop.SeedShopScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GrowGarden2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.SEED_SHOP.get(), SeedShopScreen::new);

            // Plant models use crossed planes and textures with transparent pixels.
            // CUTOUT makes fully transparent pixels invisible instead of rendering them black.
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CARROT_PLANT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATO_PLANT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DRAGON_FRUIT_PLANT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CARROT_LEAF.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATO_LEAF.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATO_FRUIT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DRAGON_STEM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DRAGON_FRUIT.get(), RenderType.cutout());
        });
    }
}
