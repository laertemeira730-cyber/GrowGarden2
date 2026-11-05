package com.growgarden2.areas;

import com.growgarden2.blocks.PlantBlock;
import com.growgarden2.plants.PlantBlockEntity;
import com.growgarden2.plants.PlantStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class PlantEvents {
    private PlantEvents() {}
    @SubscribeEvent
    public static void onPlantBroken(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        BlockPos pos = event.getPos();
        BlockPos base = PlantStructures.findBase(level, pos);
        if (base != null) {
            event.setCanceled(true);
            if (level.getBlockEntity(base) instanceof PlantBlockEntity plant && plant.getOwner() != null
                    && !plant.getOwner().equals(event.getPlayer().getUUID()))
                event.getPlayer().sendSystemMessage(Component.literal("Você não pode quebrar a estrutura de uma planta de outro jogador."));
            return;
        }
        if (!(event.getState().getBlock() instanceof PlantBlock)) return;
        if (level.getBlockEntity(pos) instanceof PlantBlockEntity plant && plant.getOwner() != null
                && !plant.getOwner().equals(event.getPlayer().getUUID())) {
            event.setCanceled(true);
            event.getPlayer().sendSystemMessage(Component.literal("Você não pode quebrar a planta de outro jogador."));
        }
    }

    @SubscribeEvent
    public static void onPlantPartClicked(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getLevel() instanceof ServerLevel level) || event.getEntity().isShiftKeyDown()) return;
        BlockPos base = PlantStructures.findBase(level, event.getPos());
        if (base == null) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.CONSUME);
        PlantBlock.interactWithPlant(level, base, event.getEntity());
    }
}
