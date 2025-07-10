package com.maxell.cha.Register;

import com.maxell.cha.CHA;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds_Max {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CHA.MODID);
    public static final RegistryObject<SoundEvent> FULL_RAGE = SOUNDS.register("fullrage",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CHA.MODID,"fullrage")));
    public static final RegistryObject<SoundEvent> RAGE_ACTIVE = SOUNDS.register("rageactive",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CHA.MODID,"rageactive")));
    public static final RegistryObject<SoundEvent> RAGE_END = SOUNDS.register("rageend",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CHA.MODID,"rageend")));
    public static final RegistryObject<SoundEvent> ADRENALINE_ACTIVE = SOUNDS.register("adrenalineactivate",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CHA.MODID,"adrenalineactivate")));
    public static final RegistryObject<SoundEvent> ADRENALINE_LOSS = SOUNDS.register("adrenalinemajorloss",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CHA.MODID,"adrenalinemajorloss")));
    public static final RegistryObject<SoundEvent> FULL_ADRENALINE = SOUNDS.register("fulladrenaline",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CHA.MODID,"fulladrenaline")));
}