package com.growgarden2.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.growgarden2.areas.AreaSavedData;
import com.growgarden2.blocks.PlantBlock;
import com.growgarden2.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlantBlockEntity extends BlockEntity {
    private UUID owner;
    private double growthProgressTicks;
    private long lastGrowthUpdate = -1L;
    private long regrowRemainingTicks;
    private boolean mature;
    private final List<BlockPos> partPositions = new ArrayList<>();
    private final List<BlockPos> fruitPositions = new ArrayList<>();

    public PlantBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.PLANT.get(), pos, state); }

    public void initialize(UUID owner) {
        if (level == null || level.isClientSide) return;
        this.owner = owner;
        growthProgressTicks = 0.0D;
        regrowRemainingTicks = 0L;
        lastGrowthUpdate = level.getGameTime();
        mature = false;
        setChanged();
        PlantStructures.rebuild((ServerLevel) level, worldPosition, owner,
                ((PlantBlock) getBlockState().getBlock()).getKind(), 0, false);
    }
    public UUID getOwner() { return owner; }
    public boolean isMature() { return mature; }
    public long getRemainingSeconds() {
        if (level == null) return 0L;
        double ticks = regrowRemainingTicks > 0 ? regrowRemainingTicks
                : Math.max(0.0D, ((PlantBlock) getBlockState().getBlock()).getKind().growthTicks() - growthProgressTicks);
        return (long) Math.ceil(ticks / (20.0D * PlantGrowthSpeed.getMultiplier()));
    }
    public List<BlockPos> getPartPositions() { return List.copyOf(partPositions); }
    public List<BlockPos> getFruitPositions() { return List.copyOf(fruitPositions); }
    public void setPartPositions(List<BlockPos> positions) {
        partPositions.clear();
        for (BlockPos pos : positions) partPositions.add(pos.immutable());
        setChanged();
    }
    public void setFruitPositions(List<BlockPos> positions) {
        fruitPositions.clear();
        for (BlockPos pos : positions) fruitPositions.add(pos.immutable());
        setChanged();
    }
    public void beginRegrowth(int ticks) {
        if (level == null) return;
        mature = false;
        regrowRemainingTicks = Math.max(1, ticks);
        lastGrowthUpdate = level.getGameTime();
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PlantBlockEntity plant) {
        if (!(level instanceof ServerLevel serverLevel) || level.getGameTime() % 20L != 0L) return;
        if (plant.owner == null || plant.lastGrowthUpdate < 0L) return;
        if (!AreaSavedData.get(serverLevel).isInsideGarden(plant.owner, pos)) return;
        long now = level.getGameTime();
        long elapsedTicks = Math.max(0L, now - plant.lastGrowthUpdate);
        plant.lastGrowthUpdate = now;
        PlantKind kind = ((PlantBlock) state.getBlock()).getKind();

        if (plant.regrowRemainingTicks > 0L) {
            plant.regrowRemainingTicks = Math.max(0L,
                    plant.regrowRemainingTicks - (long) (elapsedTicks * PlantGrowthSpeed.getMultiplier()));
            if (plant.regrowRemainingTicks == 0L) {
                plant.mature = true;
                plant.setChanged();
                PlantStructures.rebuild(serverLevel, pos, plant.owner, kind, 2, true);
            } else {
                plant.setChanged();
            }
            return;
        }
        if (plant.mature) return;
        plant.growthProgressTicks += elapsedTicks * PlantGrowthSpeed.getMultiplier();
        double total = kind.growthTicks();
        int nextStage = plant.growthProgressTicks >= total ? 2
                : (plant.growthProgressTicks >= total / 2.0D ? 1 : 0);
        boolean nextMature = plant.growthProgressTicks >= total;
        int currentStage = state.getValue(PlantBlock.STAGE);
        if (nextStage != currentStage) level.setBlock(pos, state.setValue(PlantBlock.STAGE, nextStage), 3);
        if (nextMature != plant.mature || nextStage != currentStage) {
            plant.mature = nextMature;
            plant.setChanged();
            PlantStructures.rebuild(serverLevel, pos, plant.owner, kind, nextStage, nextMature);
        } else plant.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (owner != null) tag.putUUID("Owner", owner);
        tag.putDouble("GrowthProgressTicks", growthProgressTicks);
        tag.putLong("LastGrowthUpdate", lastGrowthUpdate);
        tag.putLong("RegrowRemainingTicks", regrowRemainingTicks);
        tag.putBoolean("Mature", mature);
        savePositions(tag, "PlantParts", partPositions);
        savePositions(tag, "FruitParts", fruitPositions);
    }
    private static void savePositions(CompoundTag tag, String key, List<BlockPos> positions) {
        ListTag list = new ListTag();
        for (BlockPos pos : positions) {
            CompoundTag part = new CompoundTag();
            part.putInt("X", pos.getX()); part.putInt("Y", pos.getY()); part.putInt("Z", pos.getZ());
            list.add(part);
        }
        tag.put(key, list);
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
        growthProgressTicks = tag.getDouble("GrowthProgressTicks");
        lastGrowthUpdate = tag.getLong("LastGrowthUpdate");
        regrowRemainingTicks = tag.getLong("RegrowRemainingTicks");
        mature = tag.getBoolean("Mature");
        loadPositions(tag, "PlantParts", partPositions);
        loadPositions(tag, "FruitParts", fruitPositions);
    }
    private static void loadPositions(CompoundTag tag, String key, List<BlockPos> positions) {
        positions.clear();
        ListTag list = tag.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag part = list.getCompound(i);
            positions.add(new BlockPos(part.getInt("X"), part.getInt("Y"), part.getInt("Z")));
        }
    }
}
