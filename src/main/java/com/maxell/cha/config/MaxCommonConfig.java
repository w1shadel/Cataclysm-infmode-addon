package com.maxell.cha.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MaxCommonConfig {
    public final ForgeConfigSpec.IntValue AdrenarinePower;
    public final ForgeConfigSpec.IntValue RagePower;
    public MaxCommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("Player_Buffs");
        AdrenarinePower = buildInt(builder, "AdrenarinePower", "all", 10, 0, 1000000, "Adrenaline Power.you can use intValue.");
        RagePower = buildInt(builder, "RagePower", "all", 3, 0, 1000000, "Rage Power.you can use intValue.");
        builder.pop();
    }
    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}