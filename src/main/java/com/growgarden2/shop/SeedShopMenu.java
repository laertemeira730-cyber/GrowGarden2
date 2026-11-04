package com.growgarden2.shop;

import com.growgarden2.registry.ModItems;
import com.growgarden2.registry.ModMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;

public class SeedShopMenu extends AbstractContainerMenu {
    public static final String COINS_KEY = "GrowGarden2Coins";
    private static final int STARTING_COINS = 120;
    private final SimpleContainerData data;

    public SeedShopMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainerData(1));
    }

    private SeedShopMenu(int id, Inventory inventory, SimpleContainerData data) {
        super(ModMenus.SEED_SHOP.get(), id);
        this.data = data;
        addDataSlots(data);
        if (!inventory.player.level().isClientSide) {
            CompoundTag tag = inventory.player.getPersistentData();
            if (!tag.contains(COINS_KEY)) tag.putInt(COINS_KEY, STARTING_COINS);
            data.set(0, tag.getInt(COINS_KEY));
        }
    }

    public int getCoins() {
        return data.get(0);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (!(player instanceof ServerPlayer serverPlayer)) return false;
        Item item;
        int price;
        switch (buttonId) {
            case 0 -> { item = ModItems.CARROT_SEEDS.get(); price = 10; }
            case 1 -> { item = ModItems.TOMATO_SEEDS.get(); price = 75; }
            case 2 -> { item = ModItems.DRAGON_FRUIT_SEEDS.get(); price = 250; }
            default -> { return false; }
        }

        CompoundTag tag = serverPlayer.getPersistentData();
        if (!tag.contains(COINS_KEY)) tag.putInt(COINS_KEY, STARTING_COINS);
        int coins = tag.getInt(COINS_KEY);
        if (coins < price) return true;

        ItemStack seed = new ItemStack(item);
        if (!serverPlayer.getInventory().add(seed)) {
            serverPlayer.drop(seed, false);
        }
        coins -= price;
        tag.putInt(COINS_KEY, coins);
        data.set(0, coins);
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
