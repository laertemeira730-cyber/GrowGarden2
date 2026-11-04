package com.growgarden2.plants;

public enum PlantKind {
    CARROT("Cenoura", 3 * 60),
    TOMATO("Tomate", 15 * 60),
    DRAGON_FRUIT("Fruta-dragão", 40 * 60);

    private final String displayName;
    private final int growthSeconds;

    PlantKind(String displayName, int growthSeconds) {
        this.displayName = displayName;
        this.growthSeconds = growthSeconds;
    }

    public String displayName() {
        return displayName;
    }

    public int growthTicks() {
        return growthSeconds * 20;
    }
}
