package com.maxell.cha.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MaxCommonConfig {
    public final ForgeConfigSpec.DoubleValue AdrenarinePower;
    public final ForgeConfigSpec.DoubleValue AdrenarineX;
    public final ForgeConfigSpec.DoubleValue AdrenarineY;
    public final ForgeConfigSpec.DoubleValue RagePower;
    public final ForgeConfigSpec.DoubleValue RageX;
    public final ForgeConfigSpec.DoubleValue RageY;
    public MaxCommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("Player_Buffs");
        AdrenarinePower = buildDouble(builder, "AdrenarinePower", "all", 5, 0.0, 1000000, "Adrenaline Power.you can use doubleValue.(defaultValue:5)");
        RagePower = buildDouble(builder, "RagePower", "all", 3, 0.0, 1000000, "Rage Power.you can use doubleValue.(defaultValue:3)");
        builder.pop();
        builder.push("Adrenaline_Gauge_pos");
        AdrenarineX = buildDouble(builder, "AdrenarineX", "all", 0, -100, +100, "Changes the X coordinate of the adrenaline gauge.");
        AdrenarineY = buildDouble(builder, "AdrenarineY", "all", 0, -100, +100, "Changes the Y coordinate of the adrenaline gauge");
        builder.pop();
        builder.push("Rage_Gauge_pos");
        RageX = buildDouble(builder, "RageX", "all", 0, -100, +100, "Changes the X coordinate of the rage gauge.");
        RageY = buildDouble(builder, "RageY", "all", 0, -100, +100, "Changes the Y coordinate of the rage gauge");
        builder.pop();

    }
    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}