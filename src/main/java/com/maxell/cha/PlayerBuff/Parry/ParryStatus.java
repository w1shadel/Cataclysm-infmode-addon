package com.maxell.cha.PlayerBuff.Parry;

import com.maxell.cha.config.MInfConfig;

public class ParryStatus {
    public int parryTicks = 0;
    public boolean isParrying = false;
    public int cooldownTicks = MInfConfig.ParryCooldown; // クールダウン（残りtick）
}
