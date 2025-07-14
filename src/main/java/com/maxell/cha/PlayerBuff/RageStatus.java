package com.maxell.cha.PlayerBuff;

import com.maxell.cha.config.MInfConfig;

public class RageStatus {
    public int gauge = 0;
    public boolean pressed = false;
    public boolean isSounded = true;
    public boolean isSounded_active = false;
    public double missingtimer = MInfConfig.RageMissingTimer;
    public boolean Canmissing = MInfConfig.RageCanMissing;
}
