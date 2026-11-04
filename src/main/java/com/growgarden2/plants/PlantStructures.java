package com.growgarden2.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.growgarden2.areas.AreaSavedData;
import com.growgarden2.blocks.PlantPartBlock;
import com.growgarden2.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class PlantStructures {
    private PlantStructures() {}

    public static void rebuild(ServerLevel level, BlockPos base, UUID owner,
                               PlantKind kind, int stage, boolean mature) {
        if (!(level.getBlockEntity(base) instanceof PlantBlockEntity plant)) return;
        clearParts(level, plant);

        List<Part> structure = structureFor(kind, stage, mature);
        List<BlockPos> placed = new ArrayList<>();
        AreaSavedData areas = AreaSavedData.get(level);
        for (Part part : structure) {
            BlockPos target = base.offset(part.x(), part.y(), part.z());
            if (!areas.isInsideGarden(owner, target)) continue;
            if (!level.hasChunkAt(target)) continue;

            Block current = level.getBlockState(target).getBlock();
            if (current != Blocks.AIR && !(current instanceof PlantPartBlock)) continue;
            if (level.setBlock(target, part.block().defaultBlockState(), 3)) {
                placed.add(target.immutable());
            }
        }
        plant.setPartPositions(placed);
    }

    public static void clearParts(ServerLevel level, BlockPos base) {
        if (level.getBlockEntity(base) instanceof PlantBlockEntity plant) {
            clearParts(level, plant);
        }
    }

    private static void clearParts(ServerLevel level, PlantBlockEntity plant) {
        for (BlockPos pos : plant.getPartPositions()) {
            if (level.getBlockState(pos).getBlock() instanceof PlantPartBlock) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        plant.setPartPositions(List.of());
    }

    private static List<Part> structureFor(PlantKind kind, int stage, boolean mature) {
        List<Part> parts = new ArrayList<>();
        switch (kind) {
            case CARROT -> {
                if (stage >= 1) add(parts, ModBlocks.CARROT_LEAF.get(), 0, 1, 0);
                if (stage >= 2) {
                    add(parts, ModBlocks.CARROT_LEAF.get(), -1, 1, 0);
                    add(parts, ModBlocks.CARROT_LEAF.get(), 1, 1, 0);
                    add(parts, ModBlocks.CARROT_LEAF.get(), 0, 1, -1);
                    add(parts, ModBlocks.CARROT_LEAF.get(), 0, 1, 1);
                    add(parts, ModBlocks.CARROT_LEAF.get(), 0, 2, 0);
                }
            }
            case TOMATO -> {
                add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, 0);
                if (stage >= 1) {
                    add(parts, ModBlocks.TOMATO_LEAF.get(), -1, 1, 0);
                    add(parts, ModBlocks.TOMATO_LEAF.get(), 1, 1, 0);
                    add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, -1);
                    add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 1, 1);
                    add(parts, ModBlocks.TOMATO_LEAF.get(), 0, 2, 0);
                }
                if (stage >= 2) {
                    add(parts, ModBlocks.TOMATO_LEAF.get(), -1, 2, 0);
                    add(parts, ModBlocks.TOMATO_LEAF.get(), 1, 2, 0);
                    add(parts, ModBlocks.TOMATO_FRUIT.get(), -1, 1, 1);
                    add(parts, ModBlocks.TOMATO_FRUIT.get(), 1, 1, -1);
                    if (mature) add(parts, ModBlocks.TOMATO_FRUIT.get(), 0, 2, 1);
                }
            }
            case DRAGON_FRUIT -> {
                add(parts, ModBlocks.DRAGON_STEM.get(), 0, 1, 0);
                if (stage >= 1) {
                    add(parts, ModBlocks.DRAGON_STEM.get(), 0, 2, 0);
                    add(parts, ModBlocks.DRAGON_STEM.get(), -1, 2, 0);
                    add(parts, ModBlocks.DRAGON_STEM.get(), 1, 2, 0);
                }
                if (stage >= 2) {
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
            }
        }
        return parts;
    }

    private static void add(List<Part> parts, Block block, int x, int y, int z) {
        parts.add(new Part(block, x, y, z));
    }

    private record Part(Block block, int x, int y, int z) {}
}
