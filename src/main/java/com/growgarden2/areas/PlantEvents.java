package com.growgarden2.areas;

import com.growgarden2.blocks.PlantBlock;
import com.growgarden2.blocks.PlantPartBlock;
import com.growgarden2.plants.PlantBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class PlantEvents {
    private PlantEvents() {}

    @SubscribeEvent
    public static void onPlantBroken(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof PlantPartBlock) {
            event.setCanceled(true);
            return;
        }
        if (!(event.getState().getBlock() instanceof PlantBlock)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!(level.getBlockEntity(event.getPos()) instanceof PlantBlockEntity plant)) return;
        if (plant.getOwner() != null && !plant.getOwner().equals(event.getPlayer().getUUID())) {
            event.setCanceled(true);
            event.getPlayer().sendSystemMessage(Component.literal("Você não pode quebrar a planta de outro jogador."));
        }
    }
}
