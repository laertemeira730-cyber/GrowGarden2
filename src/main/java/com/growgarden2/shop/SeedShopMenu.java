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

public class SeedShopMenu extends AbstractContainerMenu {
    public static final String COINS_KEY = "GrowGarden2Coins";
    private static final int STARTING_COINS = 120;
    private static final int PAGE_SIZE = 3;
    private final SimpleContainerData data;

    public SeedShopMenu(int id, Inventory inventory) { this(id, inventory, new SimpleContainerData(2)); }
    private SeedShopMenu(int id, Inventory inventory, SimpleContainerData data) {
        super(ModMenus.SEED_SHOP.get(), id);
        this.data = data; addDataSlots(data);
        if (!inventory.player.level().isClientSide) {
            CompoundTag tag = inventory.player.getPersistentData();
            if (!tag.contains(COINS_KEY)) tag.putInt(COINS_KEY, STARTING_COINS);
            data.set(0, tag.getInt(COINS_KEY));
        }
    }
    public int getCoins() { return data.get(0); }
    public int getPage() { return data.get(1); }
    public int getMaxPage() { return (PlantKind.count() - 1) / PAGE_SIZE; }
    public PlantKind getVisibleKind(int row) { return PlantKind.at(getPage() * PAGE_SIZE + row); }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (!(player instanceof ServerPlayer serverPlayer)) return false;
        if (buttonId == 90) { data.set(1, Math.max(0, getPage() - 1)); return true; }
        if (buttonId == 91) { data.set(1, Math.min(getMaxPage(), getPage() + 1)); return true; }
        if (buttonId < 0 || buttonId >= PAGE_SIZE) return false;
        PlantKind kind = PlantKind.at(getPage() * PAGE_SIZE + buttonId);
        if (kind == null) return false;
        CompoundTag tag = serverPlayer.getPersistentData();
        if (!tag.contains(COINS_KEY)) tag.putInt(COINS_KEY, STARTING_COINS);
        int coins = tag.getInt(COINS_KEY);
        if (coins < kind.seedPrice()) return true;
        ItemStack seed = new ItemStack(ModItems.seedFor(kind));
        if (!serverPlayer.getInventory().add(seed)) serverPlayer.drop(seed, false);
        coins -= kind.seedPrice();
        tag.putInt(COINS_KEY, coins); data.set(0, coins);
        return true;
    }
    @Override public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
    @Override public boolean stillValid(Player player) { return true; }
}
