package com.maxell.cha.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class MConfigHolder {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final MaxCommonConfig COMMON;

    static {
        {
            final Pair<MaxCommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(MaxCommonConfig::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }
}
