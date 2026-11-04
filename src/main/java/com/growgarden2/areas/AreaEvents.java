package com.growgarden2.areas;

import com.growgarden2.areas.AreaSavedData.AreaRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class AreaEvents {
    private AreaEvents() {}

    @SubscribeEvent
    public static void onMarkerPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        AreaType type = AreaType.fromBlock(event.getPlacedBlock().getBlock());
        if (type == null) return;

        AreaSavedData data = AreaSavedData.get(level);
        BlockPos current = event.getPos().immutable();
        BlockPos first = data.getPending(player.getUUID(), type);
        if (first == null) {
            data.setPending(player.getUUID(), type, current);
            player.sendSystemMessage(Component.literal("[GrowGarden2] Primeiro canto do(a) "
                    + type.displayName() + " marcado. Coloque outro delimitador do mesmo tipo no canto oposto."));
            return;
        }
        if (first.equals(current)) return;
        data.clearPending(player.getUUID(), type);
        data.completeArea(player.getUUID(), type, first, current);
        AreaRecord created = data.getAreasFor(player.getUUID(), type).get(
                data.getAreasFor(player.getUUID(), type).size() - 1);
        player.sendSystemMessage(Component.literal("[GrowGarden2] Área de " + type.displayName()
                + " criada! Tamanho: " + created.areaInBlocks() + " blocos (" + created.minX() + ", "
                + created.minZ() + " até " + created.maxX() + ", " + created.maxZ() + ")."));
    }

    @SubscribeEvent
    public static void onMarkerBroken(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        AreaType type = AreaType.fromBlock(event.getState().getBlock());
        if (type == null) return;
        int removed = AreaSavedData.get(level).removeAt(event.getPos());
        if (removed > 0 && event.getPlayer() instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.literal("[GrowGarden2] A área de " + type.displayName()
                    + " foi removida porque um delimitador foi quebrado."));
        }
    }
}
