package com.maxell.cha.PlayerBuff;

import com.maxell.cha.CHA;
import com.maxell.cha.Client.ModKeyBindings;
import com.maxell.cha.NetworkHandler;
import com.maxell.cha.Register.ModSounds_Max;
import com.maxell.cha.config.MInfConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
@Mod.EventBusSubscriber
public class Rege {
    public static final Map<UUID, Integer> gaugeMap = new HashMap<>();
    public static final Map<UUID, Boolean> pressedMap = new HashMap<>();
    public static final Map<UUID, RageStatus> statusMap = new HashMap<>();
    public static final UUID RAGE_ATTACK_BOOST_ID = UUID.fromString("11111111-2222-3333-4444-555555555555");

    public static final AttributeModifier RAGE_ATTACK_BOOST =
            new AttributeModifier(
                    RAGE_ATTACK_BOOST_ID,
                    "Rage Mode Boost",
                    MInfConfig.RagePower - 1,
                    AttributeModifier.Operation.MULTIPLY_BASE
            );

    public static RageStatus getStatus(UUID id) {
        return statusMap.computeIfAbsent(id, k -> new RageStatus());
    }

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
                UUID id = msg.playerId;
                RageStatus status = getStatus(id);
                status.gauge = msg.gauge;
                gaugeMap.put(id, msg.gauge);
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public static class RageKeyPacket {
        public static void send() {
            NetworkHandler.CHANNEL.sendToServer(new RageKeyPacket());
        }

        public static void encode(RageKeyPacket msg, FriendlyByteBuf buf) {}
        public static RageKeyPacket decode(FriendlyByteBuf buf) { return new RageKeyPacket(); }

        public static void handle(RageKeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    UUID id = player.getUUID();
                    RageStatus status = getStatus(id);
                    if (status.gauge == 100 && !status.pressed) {
                        pressedMap.put(id, true);
                        status.pressed = true;
                    }
                }
            });
            ctx.get().setPacketHandled(true);
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
            UUID id = player.getUUID();
            RageStatus status = getStatus(id);

            int current = status.gauge;
            boolean pressed = pressedMap.getOrDefault(id, false);
            int tick = server.getTickCount();
            boolean hostileNearby = !level.getEntitiesOfClass(Monster.class,
                    new AABB(player.blockPosition()).inflate(50)).isEmpty();
            if (tick % 5 == 0 && hostileNearby && current < 100 && !status.pressed && status.isSounded) {
                status.gauge += 1;
                gaugeMap.put(id, status.gauge);
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new RageGaugeSyncPacket(status.gauge, id));
            }
            if (current == 100 && !status.isSounded_active && status.pressed) {
                level.playSound(null, player.blockPosition(), ModSounds_Max.RAGE_ACTIVE.get(), SoundSource.HOSTILE, 0.4f, 1.0f);
                status.isSounded_active = true;
            }
            if (tick % 2 == 0 && status.pressed && status.gauge > 0) {
                status.gauge = Math.max(0, status.gauge - 2);
                gaugeMap.put(id, status.gauge);
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new RageGaugeSyncPacket(status.gauge, id));
            }
            if (status.gauge == 100 && status.isSounded) {
                level.playSound(null, player.blockPosition(), ModSounds_Max.FULL_RAGE.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
                status.isSounded = false;
            }
            if (status.gauge < 100 && !status.pressed) pressedMap.put(id, false);
            if (status.gauge == 0 && status.pressed) {
                level.playSound(null, player.blockPosition(), ModSounds_Max.RAGE_END.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
                status.pressed = false;
                status.isSounded = true;
                status.isSounded_active = false;
                status.missingtimer = 60;
                pressedMap.put(id, false);
            }
            if (!hostileNearby && status.Canmissing) {
                if (status.missingtimer < 0 && tick % 2 == 0) {
                    status.gauge = Math.max(0, status.gauge - 2);
                    gaugeMap.put(id, status.gauge);
                    NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new RageGaugeSyncPacket(status.gauge, id));
                } else {
                    status.missingtimer--;
                }
            }

            AttributeInstance attr = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attr != null) {
                attr.removeModifier(RAGE_ATTACK_BOOST_ID);
                if (status.pressed && status.gauge > 0) {
                    attr.addTransientModifier(RAGE_ATTACK_BOOST);
                }
            }
        }
    }
    @Mod.EventBusSubscriber(modid = CHA.MODID, value = Dist.CLIENT)
    public static class RageModeOverlay {

        @SubscribeEvent
        public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
            if (!event.getOverlay().id().equals(VanillaGuiOverlay.DEBUG_TEXT.id())) return;
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) return;
            UUID id = player.getUUID();
            RageStatus status = getStatus(id);
            int current = status.gauge;

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            int iconWidth = 48;
            int iconHeight = 48;
            int shakeAmplitude = 3;
            int shakeX = 0;
            int shakeY = 0;
            long time = mc.level.getGameTime();
            if (status.pressed && current > 0) {
                shakeX = (int) (Math.sin(time * 0.5) * shakeAmplitude);
                shakeY = (int) (Math.cos(time * 0.5) * shakeAmplitude);
            }
            double baseX = screenWidth / 2 - iconWidth / 2 - 48 + MInfConfig.RageX + shakeX;
            double baseY = screenHeight - 80 + MInfConfig.RageY + shakeY;

            GuiGraphics gui = event.getGuiGraphics();
            ResourceLocation ICON = new ResourceLocation(CHA.MODID, "textures/gui/rage_meter.png");
            gui.blit(ICON, (int) baseX, (int) baseY, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);

            int maxGauge = 100;
            int barWidth = 36;
            int barHeight = 5;
            int filled = Math.min(current, maxGauge) * barWidth / maxGauge;

            double barX = baseX + 6;
            double barY = baseY + 24;
            gui.fill((int) barX, (int) barY, (int) (barX + filled), (int) (barY + barHeight), 0xFFFF4444);
        }
    }
}
