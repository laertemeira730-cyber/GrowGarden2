package com.growgarden2.blocks;

import com.growgarden2.plants.PlantStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PlantPartBlock extends Block {
    public PlantPartBlock(BlockBehaviour.Properties properties) { super(properties); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        BlockPos base = PlantStructures.findBase(level, pos);
        return base == null ? InteractionResult.PASS : PlantBlock.interactWithPlant(level, base, player);
    }
}
