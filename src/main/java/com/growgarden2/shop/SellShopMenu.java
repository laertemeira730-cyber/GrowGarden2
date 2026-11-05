package com.growgarden2.shop;

import com.growgarden2.plants.PlantKind;
import com.growgarden2.registry.ModItems;
import com.growgarden2.registry.ModMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class SellShopMenu extends AbstractContainerMenu {
    private static final int PAGE_SIZE = 3;
    private final SimpleContainerData data;
    public SellShopMenu(int id, Inventory inventory) { this(id, inventory, new SimpleContainerData(5)); }
    private SellShopMenu(int id, Inventory inventory, SimpleContainerData data) {
        super(ModMenus.SELL_SHOP.get(), id); this.data = data; addDataSlots(data); refreshCounts(inventory.player);
    }
    public int getCoins() { return data.get(0); }
    public int getCount(int row) { return data.get(row + 1); }
    public int getPage() { return data.get(4); }
    public int getMaxPage() { return (PlantKind.count() - 1) / PAGE_SIZE; }
    public PlantKind getVisibleKind(int row) { return PlantKind.at(getPage() * PAGE_SIZE + row); }
    public int getPrice(int row) { PlantKind kind = getVisibleKind(row); return kind == null ? 0 : kind.sellPrice(); }

    private void refreshCounts(Player player) {
        for (int row = 0; row < PAGE_SIZE; row++) {
            PlantKind kind = getVisibleKind(row); int count = 0;
            if (kind != null) for (ItemStack stack : player.getInventory().items)
                if (stack.is(ModItems.cropFor(kind))) count += stack.getCount();
            data.set(row + 1, count);
        }
        if (!player.level().isClientSide) {
            CompoundTag tag = player.getPersistentData();
            if (!tag.contains(SeedShopMenu.COINS_KEY)) tag.putInt(SeedShopMenu.COINS_KEY, 120);
            data.set(0, tag.getInt(SeedShopMenu.COINS_KEY));
        }
    }
    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (!(player instanceof ServerPlayer serverPlayer)) return false;
        if (buttonId == 90) { data.set(4, Math.max(0, getPage() - 1)); refreshCounts(serverPlayer); return true; }
        if (buttonId == 91) { data.set(4, Math.min(getMaxPage(), getPage() + 1)); refreshCounts(serverPlayer); return true; }
        if (buttonId < 0 || buttonId >= PAGE_SIZE) return false;
        PlantKind kind = getVisibleKind(buttonId); if (kind == null) return false;
        int sold = 0;
        for (ItemStack stack : serverPlayer.getInventory().items) {
            if (stack.is(ModItems.cropFor(kind))) { sold += stack.getCount(); stack.setCount(0); }
        }
        if (sold > 0) {
            CompoundTag tag = serverPlayer.getPersistentData();
            int coins = tag.getInt(SeedShopMenu.COINS_KEY) + sold * kind.sellPrice();
            tag.putInt(SeedShopMenu.COINS_KEY, coins);
            serverPlayer.getInventory().setChanged(); data.set(0, coins);
        }
        refreshCounts(serverPlayer); return true;
    }
    @Override public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
    @Override public boolean stillValid(Player player) { return true; }
}
