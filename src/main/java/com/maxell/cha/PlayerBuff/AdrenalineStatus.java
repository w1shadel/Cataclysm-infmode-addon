package com.maxell.cha.PlayerBuff;

import com.maxell.cha.config.MInfConfig;

public class AdrenalineStatus {
    public int adrenaline = 0;
    public boolean pressed = false;
    public boolean isSounded = false;
    public boolean isDamaged = false;
    public boolean isSounded_active = false;
    public double missingtimer = MInfConfig.AdrenalineMissingTimer;
    public boolean canmissing = MInfConfig.AdrenalineCanMissing;
}
