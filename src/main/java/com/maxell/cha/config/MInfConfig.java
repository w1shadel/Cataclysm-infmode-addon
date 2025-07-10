package com.maxell.cha.config;

import net.minecraftforge.fml.config.ModConfig;

public class MInfConfig {
    public static double AdrenalinePower = 6;
    public static double RagePower = 3;
    public static double AdrenarineX = 0;
    public static double AdrenarineY = 0;
    public static double RageX = 0;
    public static double RageY = 0;

    public static void bake(ModConfig config) {
        try {
            AdrenalinePower = MConfigHolder.COMMON.AdrenarinePower.get();
            RagePower = MConfigHolder.COMMON.RagePower.get();
            AdrenarineX = MConfigHolder.COMMON.AdrenarineX.get();
            AdrenarineY = MConfigHolder.COMMON.AdrenarineY.get();
            RageX = MConfigHolder.COMMON.RageX.get();
            RageY = MConfigHolder.COMMON.RageY.get();
        } catch (Exception e) {
        }
    }
}
