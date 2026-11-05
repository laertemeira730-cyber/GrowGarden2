package com.growgarden2.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.growgarden2.areas.AreaSavedData;
import com.growgarden2.blocks.PlantPartBlock;
import com.growgarden2.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class PlantStructures {
    private PlantStructures() {}

    public static void rebuild(ServerLevel level, BlockPos base, UUID owner,
                               PlantKind kind, int stage, boolean mature) {
        if (!(level.getBlockEntity(base) instanceof PlantBlockEntity plant)) return;

        // Clear tracked positions AND sweep the full footprint to remove stale parts
        // left by earlier growth stages or older saved versions of the mod.
        clearParts(level, base);
        List<Part> structure = structureFor(kind, stage, mature);
        List<BlockPos> placed = new ArrayList<>();
        AreaSavedData areas = AreaSavedData.get(level);
        for (Part part : structure) {
            BlockPos target = base.offset(part.x(), part.y(), part.z());
            if (!areas.isInsideGarden(owner, target) || !level.hasChunkAt(target)) continue;
            Block current = level.getBlockState(target).getBlock();
            if (current != Blocks.AIR && !(current instanceof PlantPartBlock)) continue;
            if (level.setBlock(target, part.block().defaultBlockState(), 3)) placed.add(target.immutable());
        }
        plant.setPartPositions(placed);
    }

    public static BlockPos findBase(Level level, BlockPos partPos) {
        for (int dy = 0; dy <= 3; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos candidate = partPos.offset(dx, -dy, dz);
                    if (level.getBlockEntity(candidate) instanceof PlantBlockEntity plant
                            && plant.getPartPositions().contains(partPos)) return candidate;
                }
            }
        }
        return null;
    }

    public static void clearParts(ServerLevel level, BlockPos base) {
        if (!(level.getBlockEntity(base) instanceof PlantBlockEntity plant)) return;
        // Known positions may be incomplete in worlds saved by an older build.
        for (BlockPos partPos : plant.getPartPositions()) {
            if (level.getBlockState(partPos).getBlock() instanceof PlantPartBlock) {
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        // All plant structures use this bounded footprint: x/z -1..1, y 1..3.
        for (BlockPos scan : BlockPos.betweenClosed(base.offset(-1, 1, -1), base.offset(1, 3, 1))) {
            if (level.getBlockState(scan).getBlock() instanceof PlantPartBlock) {
                level.setBlock(scan, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        plant.setPartPositions(List.of());
    }

    private static List<Part> structureFor(PlantKind kind, int stage, boolean mature) {
        List<Part> parts = new ArrayList<>();
        switch (kind) {
            case CARROT -> {
                switch (stage) {
                    case 1 -> add(parts, ModBlocks.CARROT_LEAF.get(), 0, 1, 0);
                    case 2 -> {
                        add(parts, ModBlocks.CARROT_LEAF.get(), 0, 1, 0);
                        add(parts, ModBlocks.CARROT_LEAF.get(), -1, 1, 0);
                        add(parts, ModBlocks.CARROT_LEAF.get(), 1, 1, 0);
                        add(parts, ModBlocks.CARROT_LEAF.get(), 0, 1, -1);
                        add(parts, ModBlocks.CARROT_LEAF.get(), 0, 1, 1);
                        add(parts, ModBlocks.CARROT_LEAF.get(), 0, 2, 0);
                    }
                    default -> { }
                }
            }
            case TOMATO -> {
                switch (stage) {
                    case 0 -> add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, 0);
                    case 1 -> {
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), -1, 1, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 1, 1, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, -1);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, 1);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 2, 0);
                    }
                    case 2 -> {
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), -1, 1, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 1, 1, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, -1);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, 1);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 2, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), -1, 2, 0);
                        add(parts, ModBlocks.TOMATO_LEAF.get(), 1, 2, 0);
                        add(parts, ModBlocks.TOMATO_FRUIT.get(), -1, 1, 1);
                        add(parts, ModBlocks.TOMATO_FRUIT.get(), 1, 1, -1);
                        if (mature) add(parts, ModBlocks.TOMATO_FRUIT.get(), 0, 2, 1);
                    }
                    default -> { }
                }
            }
            case DRAGON_FRUIT -> {
                switch (stage) {
                    case 0 -> add(parts, ModBlocks.DRAGON_STEM.get(), 0, 1, 0);
                    case 1 -> {
                        add(parts, ModBlocks.DRAGON_STEM.get(), 0, 1, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 0, 2, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), -1, 2, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 1, 2, 0);
                    }
                    case 2 -> {
                        add(parts, ModBlocks.DRAGON_STEM.get(), 0, 1, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 0, 2, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), -1, 2, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 1, 2, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 0, 3, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 0, 2, -1);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 0, 2, 1);
                        add(parts, ModBlocks.DRAGON_STEM.get(), -1, 3, 0);
                        add(parts, ModBlocks.DRAGON_STEM.get(), 1, 3, 0);
                        if (mature) {
                            add(parts, ModBlocks.DRAGON_FRUIT.get(), -1, 2, 1);
                            add(parts, ModBlocks.DRAGON_FRUIT.get(), 1, 2, -1);
                            add(parts, ModBlocks.DRAGON_FRUIT.get(), 0, 3, 1);
                        }
                    }
                    default -> { }
                }
            }
        }
        return parts;
    }

    private static void add(List<Part> parts, Block block, int x, int y, int z) {
        parts.add(new Part(block, x, y, z));
    }

    private record Part(Block block, int x, int y, int z) {}
}
