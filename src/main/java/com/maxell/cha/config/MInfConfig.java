package com.maxell.cha.config;

import net.minecraftforge.fml.config.ModConfig;

public class MInfConfig {
    public static double AdrenalinePower = 6;
    public static double RagePower = 3;
    public static double AdrenarineX = 0;
    public static double AdrenarineY = 0;
    public static double RageX = 0;
    public static double RageY = 0;
    public static double RageMissingTimer = 60;
    public static double AdrenalineMissingTimer = 60;
    public static boolean RageCanMissing = true;
    public static boolean AdrenalineCanMissing = true;
    public static double Parryknockback = 0.9;
    public static int ParryCanUse = 0;
    public static int ParryCooldown = 20;

    public static void bake(ModConfig config) {
        try {
            AdrenalinePower = MConfigHolder.COMMON.AdrenarinePower.get();
            RagePower = MConfigHolder.COMMON.RagePower.get();
            AdrenarineX = MConfigHolder.COMMON.AdrenarineX.get();
            AdrenarineY = MConfigHolder.COMMON.AdrenarineY.get();
            RageX = MConfigHolder.COMMON.RageX.get();
            RageY = MConfigHolder.COMMON.RageY.get();
            RageMissingTimer = MConfigHolder.COMMON.RageMissingTimer.get();
            AdrenalineMissingTimer = MConfigHolder.COMMON.AdrenalineMissingTimer.get();
            RageCanMissing = MConfigHolder.COMMON.RageCanMissing.get();
            AdrenalineCanMissing = MConfigHolder.COMMON.AdrenalineCanMissing.get();
            Parryknockback = MConfigHolder.COMMON.Parryknockback.get();
            ParryCanUse = MConfigHolder.COMMON.ParryCanUse.get();
            ParryCooldown = MConfigHolder.COMMON.ParryCool.get();
        } catch (Exception e) {
        }
    }
}
