package com.maxell.cha;

import com.maxell.cha.Register.ModItem_Max;
import com.maxell.cha.Register.ModSounds_Max;
import com.maxell.cha.config.MConfigHolder;
import com.maxell.cha.config.MInfConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CHA.MODID)
@Mod.EventBusSubscriber(modid = CHA.MODID)
public class CHA
{
    public static final String MODID = "cha";
    public CHA(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModSounds_Max.SOUNDS.register(modEventBus);
        ModItem_Max.ITEMS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MConfigHolder.COMMON_SPEC, "rage-and-adrenaline.toml");
        modEventBus.addListener(this::onModConfigEvent);
    }
    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        if (config.getSpec() == MConfigHolder.COMMON_SPEC) {
            MInfConfig.bake(config);
        }
    }
}
