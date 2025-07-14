package com.maxell.cha.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MaxCommonConfig {
    public final ForgeConfigSpec.DoubleValue AdrenarinePower;
    public final ForgeConfigSpec.DoubleValue AdrenarineX;
    public final ForgeConfigSpec.DoubleValue AdrenarineY;
    public final ForgeConfigSpec.DoubleValue RagePower;
    public final ForgeConfigSpec.DoubleValue RageX;
    public final ForgeConfigSpec.DoubleValue RageY;
    public final ForgeConfigSpec.DoubleValue RageMissingTimer;
    public final ForgeConfigSpec.DoubleValue AdrenalineMissingTimer;
    public final ForgeConfigSpec.BooleanValue RageCanMissing;
    public final ForgeConfigSpec.BooleanValue AdrenalineCanMissing;
    public final ForgeConfigSpec.IntValue ParryCanUse;
    public final ForgeConfigSpec.DoubleValue Parryknockback;
    public final ForgeConfigSpec.IntValue ParryCool;

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
        builder.push("Adrenaline_Can_Missing?");
        AdrenalineMissingTimer = buildDouble(builder, "AdrenalineMissingTimer", "all", 0, 0, 10000000, "When you lose sight of an enemy, you can increase or decrease the time it takes for the gauge to decrease.");
        AdrenalineCanMissing = buildBoolean(builder, "AdrenalineCanMissing", "all", true, "If you set it to True, the gauge will decrease when you lose sight of the enemy.");
        builder.pop();
        builder.push("Rage_Can_Missing?");
        RageMissingTimer = buildDouble(builder, "RageMissingTimer", "all", 0, 0, 10000000, "When you lose sight of an enemy, you can increase or decrease the time it takes for the gauge to decrease.");
        RageCanMissing = buildBoolean(builder, "RageCanMissing", "all", true, "If you set it to True, the gauge will decrease when you lose sight of the enemy.");
        builder.pop();
        builder.push("Parry_Common");
        Parryknockback = buildDouble(builder, "ParryKnockBack", "all", 0.9, 0, 10000000, "Determines the blowback when parrying.");
        ParryCanUse = buildInt(builder, "ParryCanUse?", "all", 0, 0, 2, "Determines the type of parry. (0 means it can be used without cost, 1 means it is impossible unless you use an item, 2 means it is always impossible.)");
        ParryCool = buildInt(builder, "ParryCooldown", "all", 20, 17, 1000, "You can change the cooldown time for parry.(If you set it below 17, you can become invincible by repeatedly pressing the button)");
        builder.pop();

    }
    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

}