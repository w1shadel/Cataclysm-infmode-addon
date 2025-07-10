package com.maxell.cha;

import com.maxell.cha.PlayerBuff.Adrenaline;
import com.maxell.cha.PlayerBuff.Rege;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CHA.MODID, "main"),
            () -> "1.0", s -> true, s -> true
    );
    public static void register() {
        CHANNEL.registerMessage(
                0,
                Rege.RageKeyPacket.class,
                Rege.RageKeyPacket::encode,
                Rege.RageKeyPacket::decode,
                Rege.RageKeyPacket::handle
        );
        CHANNEL.registerMessage(
                1,
                Rege.RageGaugeSyncPacket.class,
                Rege.RageGaugeSyncPacket::encode,
                Rege.RageGaugeSyncPacket::decode,
                Rege.RageGaugeSyncPacket::handle
        );
        CHANNEL.registerMessage(
                3,
                Adrenaline.AdrenalineKeyPacket.class,
                Adrenaline.AdrenalineKeyPacket::encode,
                Adrenaline.AdrenalineKeyPacket::decode,
                Adrenaline.AdrenalineKeyPacket::handle
        );
        CHANNEL.registerMessage(
                4,
                Adrenaline.AdrenalineGaugeSyncPacket.class,
                Adrenaline.AdrenalineGaugeSyncPacket::encode,
                Adrenaline.AdrenalineGaugeSyncPacket::decode,
                Adrenaline.AdrenalineGaugeSyncPacket::handle
        );
    }
}
