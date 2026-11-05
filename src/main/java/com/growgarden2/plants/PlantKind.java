package com.growgarden2.plants;

public enum PlantKind {
    MOON_BLOOM("Flor da Lua", 180 * 60, 3500, 1100, 1, "S", true, 10 * 60),
    DRAGONS_BREATH("Sopro do Dragão", 165 * 60, 3200, 1000, 1, "S", true, 10 * 60),
    GHOST_PEPPER("Pimenta Fantasma", 150 * 60, 2900, 900, 2, "S", true, 9 * 60),
    POISON_IVY("Hera Venenosa", 120 * 60, 1800, 600, 1, "S/A", true, 8 * 60),
    MUSHROOM("Cogumelo", 60 * 60, 900, 300, 1, "A", false, 0),
    VENUS_FLYTRAP("Dionaea Muscipula", 90 * 60, 1300, 450, 1, "A", true, 7 * 60),
    POMEGRANATE("Romã", 75 * 60, 1200, 400, 2, "A", true, 6 * 60),
    DRAGON_FRUIT("Fruta-do-dragão", 40 * 60, 250, 63, 2, "A", true, 5 * 60),
    SUNFLOWER("Girassol", 45 * 60, 650, 230, 1, "B", true, 4 * 60),
    BAMBOO("Bambu", 35 * 60, 550, 300, 2, "B", false, 0),
    MANGO("Manga", 45 * 60, 700, 260, 2, "B", true, 4 * 60),
    PLUM("Ameixa", 30 * 60, 400, 140, 2, "B", false, 0),
    APPLE("Maçã", 15 * 60, 200, 80, 2, "D", true, 3 * 60),
    STRAWBERRY("Morango", 12 * 60, 150, 55, 3, "D", true, 2 * 60),
    POTATO("Batata", 10 * 60, 100, 35, 2, "D", false, 0),
    CARROT("Cenoura", 3 * 60, 10, 3, 1, "D", false, 0),
    TOMATO("Tomate", 15 * 60, 75, 19, 1, "C", true, 3 * 60);

    private final String displayName;
    private final int growthSeconds, seedPrice, sellPrice, harvestAmount, regrowSeconds;
    private final String tier;
    private final boolean multiHarvest;

    PlantKind(String displayName, int growthSeconds, int seedPrice, int sellPrice,
              int harvestAmount, String tier, boolean multiHarvest, int regrowSeconds) {
        this.displayName = displayName;
        this.growthSeconds = growthSeconds;
        this.seedPrice = seedPrice;
        this.sellPrice = sellPrice;
        this.harvestAmount = harvestAmount;
        this.tier = tier;
        this.multiHarvest = multiHarvest;
        this.regrowSeconds = regrowSeconds;
    }
    public String displayName() { return displayName; }
    public int growthTicks() { return growthSeconds * 20; }
    public int seedPrice() { return seedPrice; }
    public int sellPrice() { return sellPrice; }
    public int harvestAmount() { return harvestAmount; }
    public String tier() { return tier; }
    public boolean isMultiHarvest() { return multiHarvest; }
    public int regrowTicks() { return regrowSeconds * 20; }
    public String id() { return name().toLowerCase(java.util.Locale.ROOT); }
    public static PlantKind at(int index) {
        PlantKind[] all = values();
        return index < 0 || index >= all.length ? null : all[index];
    }
    public static int count() { return values().length; }
}
