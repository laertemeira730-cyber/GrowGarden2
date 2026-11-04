package com.growgarden2.blocks;

import com.growgarden2.registry.ModBlocks;
import com.growgarden2.shop.SeedShopMenu;
import com.growgarden2.shop.SellShopMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class AreaMarkerBlock extends Block {
    public AreaMarkerBlock(BlockBehaviour.Properties properties) { super(properties); }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (state.is(ModBlocks.SHOP_MARKER.get())) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer,
                        new SimpleMenuProvider((id, inventory, p) -> new SeedShopMenu(id, inventory),
                                Component.literal("Loja de sementes")));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (state.is(ModBlocks.SALES_MARKER.get())) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer,
                        new SimpleMenuProvider((id, inventory, p) -> new SellShopMenu(id, inventory),
                                Component.literal("Central de vendas")));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
