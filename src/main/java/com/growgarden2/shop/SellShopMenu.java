package com.growgarden2.shop;

import com.growgarden2.registry.ModItems;
import com.growgarden2.registry.ModMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SellShopMenu extends AbstractContainerMenu {
    private static final Item[] CROPS = {
            ModItems.CARROT_CROP.get(), ModItems.TOMATO_CROP.get(), ModItems.DRAGON_FRUIT_CROP.get()
    };
    private static final int[] PRICES = {3, 19, 63};
    private final SimpleContainerData data;

    public SellShopMenu(int id, Inventory inventory) { this(id, inventory, new SimpleContainerData(4)); }

    private SellShopMenu(int id, Inventory inventory, SimpleContainerData data) {
        super(ModMenus.SELL_SHOP.get(), id);
        this.data = data;
        addDataSlots(data);
        refreshCounts(inventory.player);
    }

    private void refreshCounts(Player player) {
        for (int i = 0; i < CROPS.length; i++) {
            int count = 0;
            for (ItemStack stack : player.getInventory().items) if (stack.is(CROPS[i])) count += stack.getCount();
            data.set(i + 1, count);
        }
        if (!player.level().isClientSide) {
            CompoundTag tag = player.getPersistentData();
            if (!tag.contains(SeedShopMenu.COINS_KEY)) tag.putInt(SeedShopMenu.COINS_KEY, 120);
            data.set(0, tag.getInt(SeedShopMenu.COINS_KEY));
        }
    }

    public int getCoins() { return data.get(0); }
    public int getCount(int crop) { return data.get(crop + 1); }
    public int getPrice(int crop) { return PRICES[crop]; }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (!(player instanceof ServerPlayer serverPlayer) || buttonId < 0 || buttonId >= CROPS.length) return false;
        Item target = CROPS[buttonId];
        int sold = 0;
        for (ItemStack stack : serverPlayer.getInventory().items) {
            if (stack.is(target)) {
                sold += stack.getCount();
                stack.setCount(0);
            }
        }
        if (sold > 0) {
            CompoundTag tag = serverPlayer.getPersistentData();
            int coins = tag.getInt(SeedShopMenu.COINS_KEY) + sold * PRICES[buttonId];
            tag.putInt(SeedShopMenu.COINS_KEY, coins);
            serverPlayer.getInventory().setChanged();
            data.set(0, coins);
        }
        refreshCounts(serverPlayer);
        return true;
    }

    @Override public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
    @Override public boolean stillValid(Player player) { return true; }
}