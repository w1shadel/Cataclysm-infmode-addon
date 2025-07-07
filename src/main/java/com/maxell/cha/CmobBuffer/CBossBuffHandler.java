package com.maxell.cha.CmobBuffer;

import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber
public class CBossBuffHandler {
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;

        if (!level.isClientSide()) { // ✅ **サーバー側のみ処理**
            List<Mob> mobs = level.getEntitiesOfClass(Mob.class, new AABB(-500, -500, -500, 500, 500, 500)); // ✅ **広範囲でエンティティを取得**

            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.ENDER_GUARDIAN.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 12);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 3);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.IGNIS.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 12);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 3);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.ANCIENT_REMNANT.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 1.6);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 2);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.THE_HARBINGER.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 6);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 1.5);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.THE_LEVIATHAN.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 10);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 12);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.ANCIENT_ANCIENT_REMNANT.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 10);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 1.5);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }

            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.MALEDICTUS.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 20);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 3);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.NETHERITE_MONSTROSITY.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 2);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 2);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.NETHERITE_MINISTROSITY.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 2);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 2);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
            for (Mob mob : mobs) {
                if (mob.getType().equals(ModEntities.THE_BABY_LEVIATHAN.get()) &&
                        !mob.getPersistentData().getBoolean("isBuffed")) { // ✅ **ボス & 未強化の判定**

                    Optional.ofNullable(mob.getAttribute(Attributes.MAX_HEALTH)).ifPresent(health -> {
                        health.setBaseValue(health.getBaseValue() * 4);
                        mob.setHealth(mob.getMaxHealth());
                    });

                    Optional.ofNullable(mob.getAttribute(Attributes.ATTACK_DAMAGE)).ifPresent(attack -> {
                        attack.setBaseValue(attack.getBaseValue() * 20);
                    });

                    mob.getPersistentData().putBoolean("isBuffed", true); // ✅ **強化済みフラグ**
                }
            }
        }
    }
}

