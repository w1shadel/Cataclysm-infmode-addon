package com.maxell.cha.CmobBuffer;

import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber
public class CBossRemoveBuf {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if (!level.isClientSide()) { // ✅ **サーバー側のみ処理**
            List<Mob> bosses = level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10)); // ✅ **プレイヤーの近くのボスのみ取得**
            CMConfig.LeviathanDamageCap = 9999999;
            CMConfig.HarbingerDamageCap = 9999999;
            CMConfig.MaledictusDamageCap = 9999999;
            CMConfig.EnderguardianDamageCap = 9999999;
            CMConfig.AncientRemnantDamageCap = 9999999;
            CMConfig.MonstrosityDamageCap = 9999999;
            CMConfig.IgnisDamageCap = 99999999;
            if (!bosses.isEmpty()) {
                for (Mob boss : bosses) {
                    if (boss.getType().equals(ModEntities.ENDER_GUARDIAN.get())) { // ✅ **特定のボスのみ対象**
                        Collection<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects()); // ✅ **コピーを作成**

                        for (MobEffectInstance effect : effects) {
                            player.removeEffect(effect.getEffect()); // ✅ **プレイヤーのバフを即時削除**

                            if (boss instanceof LivingEntity) { // ✅ **ボスが LivingEntity なら適用**
                                ((LivingEntity) boss).addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getAmplifier() + 1));
                            }
                        }
                    }
                }
                for (Mob boss : bosses) {
                    if (boss.getType().equals(ModEntities.THE_LEVIATHAN.get())) { // ✅ **特定のボスのみ対象**
                        Collection<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects()); // ✅ **コピーを作成**

                        for (MobEffectInstance effect : effects) {
                            player.removeEffect(effect.getEffect()); // ✅ **プレイヤーのバフを即時削除**

                            if (boss instanceof LivingEntity) { // ✅ **ボスが LivingEntity なら適用**
                                ((LivingEntity) boss).addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getAmplifier() + 1));
                            }
                        }
                    }
                }
                for (Mob boss : bosses) {
                    if (boss.getType().equals(ModEntities.MALEDICTUS.get())) { // ✅ **特定のボスのみ対象**
                        Collection<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects()); // ✅ **コピーを作成**

                        for (MobEffectInstance effect : effects) {
                            player.removeEffect(effect.getEffect()); // ✅ **プレイヤーのバフを即時削除**

                            if (boss instanceof LivingEntity) { // ✅ **ボスが LivingEntity なら適用**
                                ((LivingEntity) boss).addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getAmplifier() + 1));
                            }
                        }
                    }
                }
                for (Mob boss : bosses) {
                    if (boss.getType().equals(ModEntities.IGNIS.get())) { // ✅ **特定のボスのみ対象**
                        Collection<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects()); // ✅ **コピーを作成**

                        for (MobEffectInstance effect : effects) {
                            player.removeEffect(effect.getEffect()); // ✅ **プレイヤーのバフを即時削除**

                            if (boss instanceof LivingEntity) { // ✅ **ボスが LivingEntity なら適用**
                                ((LivingEntity) boss).addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getAmplifier() + 1));
                            }
                        }
                    }
                }
                for (Mob boss : bosses) {
                    if (boss.getType().equals(ModEntities.THE_LEVIATHAN.get())) { // ✅ **特定のボスのみ対象**
                        Collection<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects()); // ✅ **コピーを作成**

                        for (MobEffectInstance effect : effects) {
                            player.removeEffect(effect.getEffect()); // ✅ **プレイヤーのバフを即時削除**

                            if (boss instanceof LivingEntity) { // ✅ **ボスが LivingEntity なら適用**
                                ((LivingEntity) boss).addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getAmplifier() + 1));
                            }
                        }
                    }
                }
                for (Mob boss : bosses) {
                    if (boss.getType().equals(ModEntities.ANCIENT_REMNANT.get())) { // ✅ **特定のボスのみ対象**
                        Collection<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects()); // ✅ **コピーを作成**

                        for (MobEffectInstance effect : effects) {
                            player.removeEffect(effect.getEffect()); // ✅ **プレイヤーのバフを即時削除**

                            if (boss instanceof LivingEntity) { // ✅ **ボスが LivingEntity なら適用**
                                ((LivingEntity) boss).addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getAmplifier() + 1));
                            }
                        }
                    }
                }
                for (Mob boss : bosses) {
                    if (boss.getType().equals(ModEntities.NETHERITE_MONSTROSITY.get())) { // ✅ **特定のボスのみ対象**
                        Collection<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects()); // ✅ **コピーを作成**

                        for (MobEffectInstance effect : effects) {
                            player.removeEffect(effect.getEffect()); // ✅ **プレイヤーのバフを即時削除**

                            if (boss instanceof LivingEntity) { // ✅ **ボスが LivingEntity なら適用**
                                ((LivingEntity) boss).addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getAmplifier() + 1));
                            }
                        }
                    }
                }
            }
        }
    }
}

