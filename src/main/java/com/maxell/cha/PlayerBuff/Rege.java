package com.maxell.cha.PlayerBuff;

import com.maxell.cha.CHA;
import com.maxell.cha.Client.ModKeyBindings;
import com.maxell.cha.Register.ModSounds;
import com.maxell.cha.config.MInfConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class Rege {
    public static final Map<UUID, Integer> gaugeMap = new HashMap<>();
    public static final Map<UUID, Boolean> pressedMap = new HashMap<>();
    public static final UUID RAGE_ATTACK_BOOST_ID = UUID.fromString("11111111-2222-3333-4444-555555555555");
    public static final AttributeModifier RAGE_ATTACK_BOOST =
            new AttributeModifier(
                    RAGE_ATTACK_BOOST_ID,
                    "Rage Mode Boost",
                    MInfConfig.RagePower, // ← 基本攻撃力に +230%（＝1.0 + 2.3 = 3.3倍）
                    AttributeModifier.Operation.MULTIPLY_BASE
            );
    public static class RageGaugeSyncPacket {
        private final int gauge;
        private final UUID playerId;

        public RageGaugeSyncPacket(int gauge, UUID playerId) {
            this.gauge = gauge;
            this.playerId = playerId;
        }

        public static void encode(RageGaugeSyncPacket msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.gauge);
            buf.writeUUID(msg.playerId);
        }

        public static RageGaugeSyncPacket decode(FriendlyByteBuf buf) {
            return new RageGaugeSyncPacket(buf.readInt(), buf.readUUID());
        }

        public static void handle(RageGaugeSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null && mc.player.getUUID().equals(msg.playerId)) {
                    Rege.gaugeMap.put(msg.playerId, msg.gauge);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
    public static class RageKeyPacket {
        public static void send() {
            NetworkHandler.CHANNEL.sendToServer(new RageKeyPacket());
        }

        public static void encode(RageKeyPacket msg, FriendlyByteBuf buf) {}
        public static RageKeyPacket decode(FriendlyByteBuf buf) {
            return new RageKeyPacket();
        }

        public static void handle(RageKeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    UUID id = player.getUUID();
                    Rege.pressedMap.put(id, true);
                }
            });
            ctx.get().setPacketHandled(true);
        }

    }
    public class NetworkHandler {
        public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(CHA.MODID, "main"),
                () -> "1.0", s -> true, s -> true
        );
        public static void register() {
            CHANNEL.registerMessage(
                    0,
                    RageKeyPacket.class,
                    RageKeyPacket::encode,
                    RageKeyPacket::decode,
                    RageKeyPacket::handle
            );
            CHANNEL.registerMessage(
                    1, // Packet ID を重複させないよう注意
                    RageGaugeSyncPacket.class,
                    RageGaugeSyncPacket::encode,
                    RageGaugeSyncPacket::decode,
                    RageGaugeSyncPacket::handle
            );
            CHANNEL.registerMessage(
                    3,
                    Adrenaline.AdrenalineKeyPacket.class,
                    Adrenaline.AdrenalineKeyPacket::encode,
                    Adrenaline.AdrenalineKeyPacket::decode,
                    Adrenaline.AdrenalineKeyPacket::handle
            );
            CHANNEL.registerMessage(
                    4, // Packet ID を重複させないよう注意
                    Adrenaline.AdrenalineGaugeSyncPacket.class,
                    Adrenaline.AdrenalineGaugeSyncPacket::encode,
                    Adrenaline.AdrenalineGaugeSyncPacket::decode,
                    Adrenaline.AdrenalineGaugeSyncPacket::handle
            );
        }
    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (ModKeyBindings.RAGE_MODE.isDown()) {
            RageKeyPacket.send();
        }
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Level level = player.level();
            BlockPos center = player.blockPosition();
            double radius = 50;
            boolean hasHostile = !level.getEntitiesOfClass(Monster.class,
                    new AABB(center).inflate(radius)).isEmpty();
            UUID id = player.getUUID();
            int current = Rege.gaugeMap.getOrDefault(id, 0);
            if (server.getTickCount() % 5 == 0 && hasHostile && current < 100 && !Pressed) {
                Rege.gaugeMap.put(id, current + 1);
                NetworkHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new RageGaugeSyncPacket(current + 1, id)
                );
            }
            boolean pressed = Rege.pressedMap.getOrDefault(id, false);
            if (current == 100 && !Pressed && pressed) {
                level.playSound(
                        null,
                        player.blockPosition(),
                        ModSounds.RAGE_ACTIVE.get(),
                        SoundSource.HOSTILE,
                        0.7f,
                        1.0f
                );
                Pressed = true;
            }
            if (server.getTickCount() % 2 == 0 & Pressed) {
                Rege.gaugeMap.put(id, current - 2);
                IsSounded = true;
                NetworkHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new RageGaugeSyncPacket(current - 2, id)
                );

            }
            if (current == 100 & IsSounded) {
                level.playSound(
                        null,
                        player.blockPosition(),
                        ModSounds.FULL_RAGE.get(),
                        SoundSource.HOSTILE,
                        1.0f,
                        1.0f
                );
                IsSounded = false;
            }
            if (current < 100 && !Pressed)
            {
                Rege.pressedMap.put(id, false); // ✅ 怒りモード終了
            }
            if (current == 0 && Pressed) {
                level.playSound(
                        null,
                        player.blockPosition(),
                        ModSounds.RAGE_END.get(),
                        SoundSource.HOSTILE,
                        1.0f,
                        1.0f
                );
                Pressed = false;
                Rege.pressedMap.put(id, false); // ✅ 怒りモード終了
            }
            AttributeInstance attr = player.getAttribute(Attributes.ATTACK_DAMAGE);
            attr.removeModifier(RAGE_ATTACK_BOOST_ID); // 古いModifierを削除
            attr.addTransientModifier(new AttributeModifier(
                    RAGE_ATTACK_BOOST_ID,
                    "Rage Boost",
                    MInfConfig.RagePower,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
            if (Pressed && current > 0) {
                if (attr != null && !attr.hasModifier(RAGE_ATTACK_BOOST)) {
                    attr.addTransientModifier(RAGE_ATTACK_BOOST);
                }
            } else {
                if (attr != null && attr.hasModifier(RAGE_ATTACK_BOOST)) {
                    attr.removeModifier(RAGE_ATTACK_BOOST_ID);
                }
            }

        }

    }
    public static boolean Pressed = false;
    public static boolean IsSounded = true;
    @Mod.EventBusSubscriber(modid = CHA.MODID, value = Dist.CLIENT)
    public class RageModeOverlay {
        @SubscribeEvent
        public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
            if (!event.getOverlay().id().equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) return;
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) return;
            GuiGraphics gui = event.getGuiGraphics();
            UUID id = player.getUUID();
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            int shakeAmplitude = 3;
            int current = Rege.gaugeMap.getOrDefault(id, 0);
            int shakeX = 0;
            int shakeY = 0;
            long time = mc.level.getGameTime();
            if (Rege.Pressed && current > 0) {
                shakeX = (int) (Math.sin(time * -0.5) * shakeAmplitude);
                shakeY = (int) (Math.cos(time * -0.5) * shakeAmplitude);
            }
            int iconWidth = 48;
            int iconHeight = 48;
            int x = screenWidth / 2 - iconWidth / 2 - 48 + shakeX;
            int y = screenHeight - 80 + shakeY;
            ResourceLocation ICON = new ResourceLocation(CHA.MODID, "textures/gui/rage_meter.png");
            gui.blit(ICON, x, y, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);;
            int maxGauge = 100;
            int barWidth = 36;
            int barHeight = 5;


            int filled = Math.min(current, maxGauge) * barWidth / maxGauge;

            int barX = x + 6 + shakeX;
            int barY = y + 24 + shakeY;  // 画像の下＋少し余白
            gui.fill(barX, barY, barX + filled, barY + barHeight, 0xFFFF4444);
        }
    }
}
