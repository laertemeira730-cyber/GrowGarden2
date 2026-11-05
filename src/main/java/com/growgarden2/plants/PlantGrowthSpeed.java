package com.growgarden2.plants;

public final class PlantGrowthSpeed {
    private static volatile double multiplier = 1.0D;

    private PlantGrowthSpeed() {}

    public static double getMultiplier() {
        return multiplier;
    }

    public static void setMultiplier(double value) {
        multiplier = Math.max(0.1D, Math.min(1000.0D, value));
    }
}
