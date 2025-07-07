package com.maxell.cha.config;

import net.minecraftforge.fml.config.ModConfig;

public class MInfConfig {
    public static long AdrenalinePower = 10;
    public static long RagePower = 3;

    public static void bake(ModConfig config) {
        try {
            AdrenalinePower = MConfigHolder.COMMON.AdrenarinePower.get();
            RagePower = MConfigHolder.COMMON.RagePower.get();
        } catch (Exception e) {
        }
    }
}
