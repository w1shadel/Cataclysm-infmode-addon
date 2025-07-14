package com.maxell.cha.PlayerBuff.Parry;

import com.maxell.cha.Client.ModKeyBindings;
import com.maxell.cha.NetworkHandler;
import com.maxell.cha.Register.ModItem_Max;
import com.maxell.cha.config.MInfConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
@Mod.EventBusSubscriber
public class Parry {
    public static final Map<UUID, ParryStatus> parryMap = new HashMap<>();
    public static ParryStatus getParryStatus(UUID id) {
        return parryMap.computeIfAbsent(id, k -> new ParryStatus());
    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (ModKeyBindings.PARRY.isDown()) {
            NetworkHandler.CHANNEL.sendToServer(new ParryKeyPacket());
        }
    }
    public static class ParryKeyPacket {
        public static void send() {
            NetworkHandler.CHANNEL.sendToServer(new ParryKeyPacket());
        }

        public static void encode(ParryKeyPacket msg, FriendlyByteBuf buf) {}
        public static ParryKeyPacket decode(FriendlyByteBuf buf) { return new ParryKeyPacket(); }

        public static void handle(ParryKeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                ParryStatus status = getParryStatus(player.getUUID());
                if (status.cooldownTicks > 0 || status.isParrying) {
                    return;
                }
                if (MInfConfig.ParryCanUse == 2) {
                    return;
                }
                if (MInfConfig.ParryCanUse == 1) {
                    boolean hasItem = false;
                    for (ItemStack stack : player.getInventory().items) {
                        if (stack.is(ModItem_Max.PARRY_CORE.get())) { // ← 判定アイテム
                            hasItem = true;
                            break;
                        }
                    }
                    if (!hasItem) {
                        return;
                    }
                }
                if (player != null) {
                    status.isParrying = true;
                    status.parryTicks = 17;
                    status.cooldownTicks = MInfConfig.ParryCooldown;
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            ParryStatus status = getParryStatus(id);
            if (status.parryTicks > 0) {
                status.parryTicks--;
                if (status.parryTicks == 0) {
                    status.isParrying = false;
                }
            }
            if (status.cooldownTicks > 0) {
                status.cooldownTicks--;
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ParryStatus status = getParryStatus(player.getUUID());
        if (status.isParrying && status.parryTicks > 0 && event.getSource().getEntity() instanceof LivingEntity attacker) {
            event.setCanceled(true);
            Vec3 knockback = attacker.position().subtract(player.position()).normalize().scale(MInfConfig.Parryknockback);
            attacker.hurtMarked = true;
            attacker.setDeltaMovement(knockback);
            attacker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));
            attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
            player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0f, 1.0f);
            player.level().addParticle(ParticleTypes.EXPLOSION, attacker.getX(), attacker.getY(), attacker.getZ(), 0.0, 0.0, 0.0);
            status.isParrying = false;
            status.parryTicks = 0;
            status.cooldownTicks = 0;
        }

    }

}
