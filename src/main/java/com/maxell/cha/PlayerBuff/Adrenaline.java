package com.maxell.cha.PlayerBuff;

import com.maxell.cha.CHA;
import com.maxell.cha.Client.ModKeyBindings;
import com.maxell.cha.Register.ModSounds;
import com.maxell.cha.config.MInfConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
public class Adrenaline {
    public static final Map<UUID, Integer> adrenalineMap = new HashMap<>();
    public static final UUID ADRENALINE_ATTACK_BOOST_ID = UUID.fromString("99999999-8888-7777-6666-555555555555");
    public static final Map<UUID, Boolean> pressedMap = new HashMap<>();
    public static final AttributeModifier ADRENALINE_ATTACK_BOOST =
            new AttributeModifier(ADRENALINE_ATTACK_BOOST_ID, "Adrenaline Mode Boost", MInfConfig.AdrenalinePower, AttributeModifier.Operation.MULTIPLY_BASE);
    public static class AdrenalineGaugeSyncPacket {
        private final int gauge;
        private final UUID playerId;

        public AdrenalineGaugeSyncPacket(int gauge, UUID playerId) {
            this.gauge = gauge;
            this.playerId = playerId;
        }

        public static void encode(AdrenalineGaugeSyncPacket msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.gauge);
            buf.writeUUID(msg.playerId);
        }

        public static AdrenalineGaugeSyncPacket decode(FriendlyByteBuf buf) {
            return new AdrenalineGaugeSyncPacket(buf.readInt(), buf.readUUID());
        }

        public static void handle(AdrenalineGaugeSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null && mc.player.getUUID().equals(msg.playerId)) {
                    Adrenaline.adrenalineMap.put(msg.playerId, msg.gauge);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
    public static class AdrenalineKeyPacket {
        public static void send() {
            Rege.NetworkHandler.CHANNEL.sendToServer(new AdrenalineKeyPacket());
        }

        public static void encode(AdrenalineKeyPacket msg, FriendlyByteBuf buf) {}
        public static AdrenalineKeyPacket decode(FriendlyByteBuf buf) {
            return new AdrenalineKeyPacket();
        }

        public static void handle(AdrenalineKeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    UUID id = player.getUUID();
                    Adrenaline.pressedMap.put(id, true);
                }
            });
            ctx.get().setPacketHandled(true);
        }

    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (ModKeyBindings.Adrenaline_MODE.isDown()) {
            AdrenalineKeyPacket.send();
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
            int current = Adrenaline.adrenalineMap.getOrDefault(id, 0);
            boolean pressed = Adrenaline.pressedMap.getOrDefault(id, false);
            if (server.getTickCount() % 5 == 0 && hasHostile && current < 100 && !Pressed) {
                Adrenaline.adrenalineMap.put(id, (current + 2));
                IsDamaged = true;
                Rege.NetworkHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new AdrenalineGaugeSyncPacket(current + 1, id)
                );
            }
            if (current < 100 && !Pressed)
            {
                Adrenaline.pressedMap.put(id, false); // ✅ 怒りモード終了
            }
            if (current == 100 && !Pressed && pressed) {
                level.playSound(
                        null,
                        player.blockPosition(),
                        ModSounds.ADRENALINE_ACTIVE.get(),
                        SoundSource.HOSTILE,
                        0.4f,
                        1.0f
                );
                Pressed = true;
            }
            if (server.getTickCount() % 2 == 0 & Pressed) {
                Adrenaline.adrenalineMap.put(id, current - 2);
                IsSounded = true;
                IsDamaged = true;
                Rege.NetworkHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new AdrenalineGaugeSyncPacket(current - 2, id)
                );
            }
            if (current == 100 & IsSounded) {
                level.playSound(
                        null,
                        player.blockPosition(),
                        ModSounds.FULL_ADRENALINE.get(),
                        SoundSource.HOSTILE,
                        0.5f,
                        1.0f
                );
                IsSounded = false;
            }
            AttributeInstance attr = player.getAttribute(Attributes.ATTACK_DAMAGE);
            attr.removeModifier(ADRENALINE_ATTACK_BOOST); // 古いModifierを削除
            attr.addTransientModifier(new AttributeModifier(
                    ADRENALINE_ATTACK_BOOST_ID,
                    "Rage Boost",
                    MInfConfig.AdrenalinePower,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
            if (current == 0)
            {
                Pressed = false;
            }
            if (Pressed && current > 0) {
                if (attr != null && !attr.hasModifier(ADRENALINE_ATTACK_BOOST)) {
                    attr.addTransientModifier(ADRENALINE_ATTACK_BOOST);
                }
            } else {
                if (attr != null && attr.hasModifier(ADRENALINE_ATTACK_BOOST)) {
                    attr.removeModifier(ADRENALINE_ATTACK_BOOST);
                }
            }
        }

    }
    @Mod.EventBusSubscriber(modid = CHA.MODID)
    public class AdrenalineDamageHandler {

        @SubscribeEvent
        public static void onPlayerHurt(LivingHurtEvent event) {
            if (!(event.getEntity() instanceof ServerPlayer player)) return;
            if (Adrenaline.Pressed)return;
            UUID id = player.getUUID();
            int adrenaline = Adrenaline.adrenalineMap.getOrDefault(id, 0);
            Level level = player.level();
            if (adrenaline >= 100) {
                float originalDamage = event.getAmount();
                float reduced = originalDamage * 0.65f;
                event.setAmount(reduced);
                level.playSound(
                        null,
                        player.blockPosition(),
                        ModSounds.ADRENALINE_LOSS.get(),
                        SoundSource.HOSTILE,
                        1.0f,
                        1.0f
                );
            }

            Adrenaline.adrenalineMap.put(id, 0);
        }
    }


    public static boolean Pressed = false;
    public static boolean IsSounded = true;
    public static boolean IsDamaged = true;

    @Mod.EventBusSubscriber(modid = CHA.MODID, value = Dist.CLIENT)
    public class adrenaline_meterOverray {
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
            int current = Adrenaline.adrenalineMap.getOrDefault(id, 0);
            int shakeAmplitude = 2;
            int shakeX = 0;
            int shakeY = 0;
            long time = mc.level.getGameTime();
            if (Adrenaline.Pressed && current > 0) {
                shakeX = (int) (Math.sin(time * 0.5) * shakeAmplitude);
                shakeY = (int) (Math.cos(time * 0.5) * shakeAmplitude);
            }
            // 🎯 画像サイズと位置
            int iconWidth = 48;
            int iconHeight = 48;
            int x = screenWidth / 2 - iconWidth / 2 - 48 - 48 + shakeX;
            int y = screenHeight - 79 + shakeY;
            ResourceLocation ICON = new ResourceLocation(CHA.MODID, "textures/gui/adrenaline_meter.png");
            gui.blit(ICON, x, y, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);

            // 📊 ゲージ取得
            int gauge = (int) Adrenaline.adrenalineMap.getOrDefault((Object) player.getUUID(), (int) 0.0);
            int maxGauge = 100;
            int barWidth = 36 ;
            int barHeight = 5;
            int filled = Math.min(gauge, maxGauge) * barWidth / maxGauge;

            int barX = x + 6 + shakeX;
            int barY = y + 23 + shakeY;  // 画像の下＋少し余白
            gui.fill(barX, barY, barX + filled, barY + barHeight, 0xFF44FF44);
        }
    }
}
