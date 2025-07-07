package com.maxell.cha;

import com.maxell.cha.PlayerBuff.Adrenaline;
import com.maxell.cha.PlayerBuff.Rege;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CHA.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Rege.NetworkHandler::register); // ✅ 安全な登録タイミング
    }
}
