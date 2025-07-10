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
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class Adrenaline {
    public static final Map<UUID, AdrenalineStatus> statusMap = new HashMap<>();
    public static final UUID BOOST_ID = UUID.fromString("99999999-8888-7777-6666-555555555555");

    public static AdrenalineStatus getStatus(UUID id) {
        return statusMap.computeIfAbsent(id, k -> new AdrenalineStatus());
    }

    public static class AdrenalineGaugeSyncPacket {
        private final int gauge;
        private final UUID playerId;

        public AdrenalineGaugeSyncPacket(int g, UUID id) { gauge = g; playerId = id; }
        public static void encode(AdrenalineGaugeSyncPacket msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.gauge); buf.writeUUID(msg.playerId);
        }
        public static AdrenalineGaugeSyncPacket decode(FriendlyByteBuf buf) {
            return new AdrenalineGaugeSyncPacket(buf.readInt(), buf.readUUID());
        }
        public static void handle(AdrenalineGaugeSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (Objects.requireNonNull(Minecraft.getInstance().player).getUUID().equals(msg.playerId)) {
                    getStatus(msg.playerId).adrenaline = msg.gauge;
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public static class AdrenalineKeyPacket {
        public static void send() {
            NetworkHandler.CHANNEL.sendToServer(new AdrenalineKeyPacket());
        }
        public static void encode(AdrenalineKeyPacket msg, FriendlyByteBuf buf) {}
        public static AdrenalineKeyPacket decode(FriendlyByteBuf buf) { return new AdrenalineKeyPacket(); }
        public static void handle(AdrenalineKeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                UUID id = player.getUUID();
                AdrenalineStatus s = getStatus(id);

                // ✅ ゲージが100未満なら発動させない
                if (s.adrenaline == 100 && !s.pressed) {
                    s.pressed = true;
                }
            }
            ctx.get().setPacketHandled(true);
        }

    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (Minecraft.getInstance().player == null) return;
        if (ModKeyBindings.Adrenaline_MODE.isDown()) AdrenalineKeyPacket.send();
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            AdrenalineStatus s = getStatus(id);
            int tick = Objects.requireNonNull(player.level().getServer()).getTickCount();
            int current = s.adrenaline;
            Level level = player.level();
            AttributeInstance attr = player.getAttribute(Attributes.ATTACK_DAMAGE);
            boolean hasHostile = !level.getEntitiesOfClass(Monster.class, new AABB(player.blockPosition()).inflate(50)).isEmpty();

            if (tick % 5 == 0 && hasHostile && current < 100 && !s.pressed) {
                s.adrenaline += 2;
                s.isDamaged = true;
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new AdrenalineGaugeSyncPacket(s.adrenaline, id));
            }

            if (current == 100 && !s.isSounded_active && s.pressed) {
                level.playSound(null, player.blockPosition(), ModSounds_Max.ADRENALINE_ACTIVE.get(), SoundSource.HOSTILE, 0.4f, 1.0f);
                s.isSounded_active = true;
            }

            if (tick % 2 == 0 && s.pressed && s.adrenaline > 0){
                s.adrenaline = Math.max(0, s.adrenaline - 2);
                s.isSounded = true;
                s.isDamaged = true;
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new AdrenalineGaugeSyncPacket(s.adrenaline, id));
            }
            if (s.adrenaline == 100 && s.isSounded) {
                level.playSound(null, player.blockPosition(), ModSounds_Max.FULL_ADRENALINE.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
                s.isSounded = false;
            }

            if (attr != null) {
                attr.removeModifier(BOOST_ID);
                if (s.pressed && s.adrenaline > 0) {
                    attr.addTransientModifier(new AttributeModifier(BOOST_ID, "Adrenaline Boost", MInfConfig.AdrenalinePower, AttributeModifier.Operation.MULTIPLY_BASE));
                }
            }

            if (s.adrenaline == 0) s.pressed = false;
            if (s.adrenaline == 0) s.isSounded_active = false;
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer p)) return;
        AdrenalineStatus s = getStatus(p.getUUID());
        if (s.pressed) return;
        if (s.adrenaline >= 100) {
            e.setAmount(e.getAmount() * 0.5f);
            p.level().playSound(null, p.blockPosition(), ModSounds_Max.ADRENALINE_LOSS.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
        }
        s.adrenaline = 0;
    }

    @Mod.EventBusSubscriber(modid = CHA.MODID, value = Dist.CLIENT)
    public static class Overlay {
        @SubscribeEvent
        public static void render(RenderGuiOverlayEvent.Pre e) {
            if (!e.getOverlay().id().equals(VanillaGuiOverlay.DEBUG_TEXT.id())) return;
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer p = mc.player;
            if (p == null) return;
            UUID id = p.getUUID();
            AdrenalineStatus s = getStatus(id);
            GuiGraphics gui = e.getGuiGraphics();

            int w = mc.getWindow().getGuiScaledWidth(), h = mc.getWindow().getGuiScaledHeight();
            long time = mc.level.getGameTime();
            int shake = (s.pressed && s.adrenaline > 0) ? 2 : 0;

            int shakeX = (int) (Math.sin(time * 0.5) * shake);
            int shakeY = (int) (Math.cos(time * 0.5) * shake);
            int iconW = 48, iconH = 48;
            int baseX = (int) (w / 2 - iconW - 88 + shakeX - MInfConfig.AdrenarineX);
            int baseY = (int) (h - 79 + shakeY - MInfConfig.AdrenarineY);
            gui.blit(new ResourceLocation(CHA.MODID, "textures/gui/adrenaline_meter.png"), baseX, baseY, 0, 0, iconW, iconH, iconW, iconH);

            int barW = 36, barH = 5, filled = Math.min(s.adrenaline, 100) * barW / 100;
            gui.fill(baseX + 6, baseY + 23, baseX + 6 + filled, baseY + 23 + barH, 0xFF44FF44);
        }
    }
}
