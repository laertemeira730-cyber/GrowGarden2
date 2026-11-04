package com.growgarden2.areas;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class AreaSavedData extends SavedData {
    private static final String DATA_NAME = "growgarden2_areas";
    private final List<AreaRecord> areas = new ArrayList<>();
    private final List<PendingRecord> pending = new ArrayList<>();

    public static AreaSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(AreaSavedData::load, AreaSavedData::new, DATA_NAME);
    }

    public static AreaSavedData load(CompoundTag root) {
        AreaSavedData data = new AreaSavedData();
        ListTag areaList = root.getList("Areas", Tag.TAG_COMPOUND);
        for (int i = 0; i < areaList.size(); i++) {
            CompoundTag tag = areaList.getCompound(i);
            try {
                data.areas.add(new AreaRecord(tag.getUUID("Owner"), AreaType.valueOf(tag.getString("Type")),
                        readPos(tag, "A"), readPos(tag, "B")));
            } catch (IllegalArgumentException ignored) {}
        }
        ListTag pendingList = root.getList("Pending", Tag.TAG_COMPOUND);
        for (int i = 0; i < pendingList.size(); i++) {
            CompoundTag tag = pendingList.getCompound(i);
            try {
                data.pending.add(new PendingRecord(tag.getUUID("Owner"), AreaType.valueOf(tag.getString("Type")),
                        readPos(tag, "Pos")));
            } catch (IllegalArgumentException ignored) {}
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag root) {
        ListTag areaList = new ListTag();
        for (AreaRecord area : areas) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("Owner", area.owner());
            tag.putString("Type", area.type().name());
            writePos(tag, "A", area.cornerA());
            writePos(tag, "B", area.cornerB());
            areaList.add(tag);
        }
        root.put("Areas", areaList);
        ListTag pendingList = new ListTag();
        for (PendingRecord marker : pending) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("Owner", marker.owner());
            tag.putString("Type", marker.type().name());
            writePos(tag, "Pos", marker.pos());
            pendingList.add(tag);
        }
        root.put("Pending", pendingList);
        return root;
    }

    public BlockPos getPending(UUID owner, AreaType type) {
        for (PendingRecord marker : pending)
            if (marker.owner().equals(owner) && marker.type() == type) return marker.pos();
        return null;
    }

    public void setPending(UUID owner, AreaType type, BlockPos pos) {
        clearPending(owner, type);
        pending.add(new PendingRecord(owner, type, pos.immutable()));
        setDirty();
    }

    public void clearPending(UUID owner, AreaType type) {
        if (pending.removeIf(marker -> marker.owner().equals(owner) && marker.type() == type)) setDirty();
    }

    public void completeArea(UUID owner, AreaType type, BlockPos first, BlockPos second) {
        areas.add(new AreaRecord(owner, type, first.immutable(), second.immutable()));
        setDirty();
    }

    public int removeAt(BlockPos pos) {
        int before = areas.size();
        areas.removeIf(area -> area.cornerA().equals(pos) || area.cornerB().equals(pos));
        int removed = before - areas.size();
        boolean pendingRemoved = pending.removeIf(marker -> marker.pos().equals(pos));
        if (removed > 0 || pendingRemoved) setDirty();
        return removed;
    }

    public List<AreaRecord> getAreasFor(UUID owner, AreaType type) {
        List<AreaRecord> result = new ArrayList<>();
        for (AreaRecord area : areas)
            if (area.owner().equals(owner) && area.type() == type) result.add(area);
        return List.copyOf(result);
    }

    private static BlockPos readPos(CompoundTag tag, String key) {
        CompoundTag pos = tag.getCompound(key);
        return new BlockPos(pos.getInt("X"), pos.getInt("Y"), pos.getInt("Z"));
    }

    private static void writePos(CompoundTag tag, String key, BlockPos value) {
        CompoundTag pos = new CompoundTag();
        pos.putInt("X", value.getX());
        pos.putInt("Y", value.getY());
        pos.putInt("Z", value.getZ());
        tag.put(key, pos);
    }

    public record AreaRecord(UUID owner, AreaType type, BlockPos cornerA, BlockPos cornerB) {
        public int minX() { return Math.min(cornerA.getX(), cornerB.getX()); }
        public int maxX() { return Math.max(cornerA.getX(), cornerB.getX()); }
        public int minZ() { return Math.min(cornerA.getZ(), cornerB.getZ()); }
        public int maxZ() { return Math.max(cornerA.getZ(), cornerB.getZ()); }
        public boolean contains(BlockPos pos) {
            return pos.getX() >= minX() && pos.getX() <= maxX()
                    && pos.getZ() >= minZ() && pos.getZ() <= maxZ();
        }
        public long areaInBlocks() {
            return (long) (maxX() - minX() + 1) * (maxZ() - minZ() + 1);
        }
    }
    private record PendingRecord(UUID owner, AreaType type, BlockPos pos) {}
}
