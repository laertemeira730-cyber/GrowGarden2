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
    private boolean mature;
    private final List<BlockPos> partPositions = new ArrayList<>();

    public PlantBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLANT.get(), pos, state);
    }

    public void initialize(UUID owner) {
        if (level == null || level.isClientSide) return;
        this.owner = owner;
        this.growthProgressTicks = 0.0D;
        this.lastGrowthUpdate = level.getGameTime();
        this.mature = false;
        setChanged();
        PlantStructures.rebuild((ServerLevel) level, worldPosition, owner,
                ((PlantBlock) getBlockState().getBlock()).getKind(), 0, false);
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isMature() {
        return mature;
    }

    public long getRemainingSeconds() {
        if (level == null) return 0L;
        long remainingTicks = Math.max(0L,
                ((PlantBlock) getBlockState().getBlock()).getKind().growthTicks()
                        - (long) growthProgressTicks);
        return (long) Math.ceil(remainingTicks / (20.0D * PlantGrowthSpeed.getMultiplier()));
    }

    public List<BlockPos> getPartPositions() {
        return List.copyOf(partPositions);
    }

    public void setPartPositions(List<BlockPos> positions) {
        partPositions.clear();
        for (BlockPos pos : positions) partPositions.add(pos.immutable());
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PlantBlockEntity plant) {
        if (!(level instanceof ServerLevel serverLevel) || level.getGameTime() % 20L != 0L) return;
        if (plant.owner == null || plant.lastGrowthUpdate < 0L) return;
        if (!AreaSavedData.get(serverLevel).isInsideGarden(plant.owner, pos)) return;

        long now = level.getGameTime();
        long elapsedTicks = Math.max(0L, now - plant.lastGrowthUpdate);
        plant.lastGrowthUpdate = now;
        plant.growthProgressTicks += elapsedTicks * PlantGrowthSpeed.getMultiplier();

        PlantKind kind = ((PlantBlock) state.getBlock()).getKind();
        double totalGrowthTicks = kind.growthTicks();
        int nextStage = plant.growthProgressTicks >= totalGrowthTicks ? 2
                : (plant.growthProgressTicks >= totalGrowthTicks / 2.0D ? 1 : 0);
        boolean nextMature = plant.growthProgressTicks >= totalGrowthTicks;
        int currentStage = state.getValue(PlantBlock.STAGE);
        if (nextStage == currentStage && nextMature == plant.mature) {
            plant.setChanged();
            return;
        }

        if (nextStage != currentStage) {
            level.setBlock(pos, state.setValue(PlantBlock.STAGE, nextStage), 3);
        }
        plant.mature = nextMature;
        plant.setChanged();
        PlantStructures.rebuild(serverLevel, pos, plant.owner, kind, nextStage, nextMature);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (owner != null) tag.putUUID("Owner", owner);
        tag.putDouble("GrowthProgressTicks", growthProgressTicks);
        tag.putLong("LastGrowthUpdate", lastGrowthUpdate);
        tag.putBoolean("Mature", mature);

        ListTag parts = new ListTag();
        for (BlockPos pos : partPositions) {
            CompoundTag part = new CompoundTag();
            part.putInt("X", pos.getX());
            part.putInt("Y", pos.getY());
            part.putInt("Z", pos.getZ());
            parts.add(part);
        }
        tag.put("PlantParts", parts);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
        growthProgressTicks = tag.getDouble("GrowthProgressTicks");
        lastGrowthUpdate = tag.getLong("LastGrowthUpdate");
        mature = tag.getBoolean("Mature");
        partPositions.clear();
        ListTag parts = tag.getList("PlantParts", Tag.TAG_COMPOUND);
        for (int i = 0; i < parts.size(); i++) {
            CompoundTag part = parts.getCompound(i);
            partPositions.add(new BlockPos(part.getInt("X"), part.getInt("Y"), part.getInt("Z")));
        }
    }
}
